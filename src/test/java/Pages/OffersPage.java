package Pages;

import Base.BasePage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffersPage extends BasePage {

    protected By offersButton = By.xpath("//div[contains(text(),'შეთავაზებები')]/ancestor::a");

    public OffersPage(WebDriver driver) {
        super(driver);
    }

    public void openOffersPage() {
        waitAndGet(offersButton).click();
        String originalWindow = driver.getWindowHandle();
        wait.until(driver -> driver.getWindowHandles().size() > 1);
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                closePopUp();
                break;
            }
        }
    }

    public void scrollToBottomUntilLoaded() {
        long lastHeight = getScrollHeight();

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            final long previousHeight = lastHeight;
            boolean heightIncreased = false;

            try {
                heightIncreased = wait.until(driver -> {
                    long newHeight = getScrollHeight();
                    return newHeight > previousHeight;
                });
            } catch (TimeoutException e) {
                heightIncreased = false;
            }

            long newHeight = getScrollHeight();
            if (!heightIncreased || newHeight == lastHeight) {
                break;
            }
            lastHeight = newHeight;
        }
    }


    public static List<Map<String, String>> scrapeProducts() {
        List<WebElement> productCards = driver.findElements(By.cssSelector("div.ProductCard_container__7IE0M"));
        List<Map<String, String>> data = new ArrayList<>();

        for (WebElement card : productCards) {
            Map<String, String> row = new HashMap<>();
            row.put("Title", getAttributeSafely(card, "a.ProductCard_title__Rpp75 > span", "title"));
            row.put("Discount", getTextSafely(card, ".Label_label__EnQXP"));
            row.put("New Price", getTextSafely(card, ".ProductCard_productInfo__price__NyCJR span"));
            row.put("Old Price", getTextSafely(card, ".ProductCard_productInfo__price_discount__CXdp2 span"));

            String imgLink = getAttributeSafely(card, "figure.ProductCard_image__zCC4j > img", "src");
            row.put("Image Link", imgLink);
            row.put("Product Link", formatProductLink(imgLink));

            data.add(row);
        }

        return data;
    }

    private static String getTextSafely(WebElement parent, String cssSelector) {
        try {
            return parent.findElement(By.cssSelector(cssSelector)).getText();
        } catch (Exception e) {
            return null;
        }
    }

    private static String getAttributeSafely(WebElement parent, String cssSelector, String attribute) {
        try {
            return parent.findElement(By.cssSelector(cssSelector)).getAttribute(attribute);
        } catch (Exception e) {
            return null;
        }
    }

    private static String formatProductLink(String imgLink) {
        if (imgLink == null) return null;
        return imgLink
                .replace("/cdn.", "")
                .replace("2nabiji.ge", "2nabiji.ge/ge")
                .replace("products", "product")
                .replace("-300x300.webp", "#");
    }


    public static void saveToExcel(List<Map<String, String>> data, String fileName, String sheetName) {
        Workbook workbook = new XSSFWorkbook();
        if (data.isEmpty()) {
            System.out.println("No data to write.");
            return;
        }

        try (
                FileOutputStream outputStream = new FileOutputStream(fileName)) {
            Sheet sheet = workbook.createSheet(sheetName);
            Map<String, String> firstRow = data.get(0);
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;
            for (String key : firstRow.keySet()) {
                headerRow.createCell(colIndex++).setCellValue(key);
            }

            int rowIndex = 1;
            for (Map<String, String> rowData : data) {
                Row row = sheet.createRow(rowIndex++);
                colIndex = 0;
                for (String key : firstRow.keySet()) {
                    row.createCell(colIndex++)
                            .setCellValue(rowData.getOrDefault(key, ""));
                }
            }

            for (int i = 0; i < firstRow.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
            System.out.println("Excel file saved: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void marketScraper() {
        try {
            List<Map<String, String>> data2Nabiji = scrapeProducts();
            Thread.sleep(2000);
            saveToExcel(data2Nabiji, "market_discounts.xlsx", "2nabiji");
            System.out.println("Scraping finished. Data saved to market_discounts.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}

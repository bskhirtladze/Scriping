package Base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class BasePage {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static JavascriptExecutor js;
    protected By closePopUpButton = By.cssSelector("img[class*='MobileBanner_closeBanner']");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    protected WebElement waitAndGet(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected long getScrollHeight() {
        return ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
    }

    public void closePopUp() {
        waitAndGet(closePopUpButton).click();
    }

}

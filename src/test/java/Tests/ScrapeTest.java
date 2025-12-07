package Tests;

import Base.BasePage;
import Base.BaseTest;
import Pages.OffersPage;
import org.testng.annotations.Test;

public class ScrapeTest extends BaseTest {
    @Test(description = "Scrape product discount data from 2nabiji market")
    public void scrape() {

        BasePage basePage = new BasePage(driver);
        OffersPage offersPage = new OffersPage(driver);

        basePage.closePopUp();
        offersPage.openOffersPage();
        offersPage.scrollToBottomUntilLoaded();
        OffersPage.scrapeProducts();
        offersPage.marketScraper();
        System.out.println("wow");
    }
}

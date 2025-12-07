# Market Discounts Scraper
This scraper automates the process of navigating to an offers page, loading all dynamic content, extracting product data, and saving it. It is built using Java + Selenium WebDriver and follows the Page Object Model (POM) structure.

---

## Project Structure
```
project
├─ base
│ ├─ BasePage.java
│ └─ BaseTest.java
├─ pages
│ └─ OffersPage.java
└─ tests
└─ ScrapeTest.java
```

---

## Requirements
Software & Runtime
- Java 17+ (recommended: Temurin, OpenJDK, or Oracle JDK)
- Maven or Gradle (Maven preferred)
- Chrome Browser (or any browser you configure)
- ChromeDriver (automatically managed if using WebDriverManager)

---

## Features
- Scrapes:
    - product names
    - discounts
    - old and new prices
    - product links
    - product images (2nabiji)
- Infinite scroll handling for 2nabiji
- Cleans and formats text
- Saves the results into an Excel file with two sheets

---

## Running the Scraper
Run using Java directly:
```

Output file:

```
market_discounts.xlsx
```

Sheets created:
- 2nabiji

---

## License
MIT License

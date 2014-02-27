package br.com.devopsnapratica.acceptance;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SearchTest {
    private WebDriver driver;

    @Before
    public void openBrowser() throws InterruptedException {
        driver = new HtmlUnitDriver();
        driver.get("http://localhost:8080/");
        Thread.sleep(5000); // Waiting for Solr index
    }

    @Test
    public void searchScenario() throws IOException {
        assertThat(driver.getTitle(),
                is("Broadleaf Demo - Heat Clinic"));

        WebElement searchField = driver.findElement(
                By.name("q"));
        searchField.sendKeys("hot sauce");
        searchField.submit();

        WebElement searchResults = driver.findElement(
                By.cssSelector("#left_column > header > h1"));
        assertThat(searchResults.getText(),
                is("Search Results hot sauce (1 - 15 of 18)"));
        List<WebElement> results = driver.findElements(
                By.cssSelector("ul#products > li"));
        assertThat(results.size(), is(15));
    }
}

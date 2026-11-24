package br.ifsp.demo.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseSeleniumTest {

    protected WebDriver driver;


    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        setInitialPage();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Hook method that can be overridden to set the initial test page
    protected void setInitialPage(){

    }
}

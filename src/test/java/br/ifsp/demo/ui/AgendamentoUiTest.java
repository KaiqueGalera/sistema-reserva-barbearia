package br.ifsp.demo.ui;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@Tag("UiTest")
public class AgendamentoUiTest {
    private WebDriver driver;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        faker = Faker.instance();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}

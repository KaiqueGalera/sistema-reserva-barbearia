package br.ifsp.demo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By emailInput = By.cssSelector("input[autocomplete='email']");
    private final By senhaInput = By.cssSelector("input[autocomplete='current-password']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage preencherEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput))
                .sendKeys(email);

        return this;
    }

    public LoginPage preencherSenha(String senha) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(senhaInput)).sendKeys(senha);

        return this;
    }
}

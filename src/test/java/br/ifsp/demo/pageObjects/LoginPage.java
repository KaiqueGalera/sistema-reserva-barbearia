package br.ifsp.demo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By emailInput = By.cssSelector("input[autocomplete='email']");
    private final By senhaInput = By.cssSelector("input[autocomplete='current-password']");
    private final By botaoEntrar = By.cssSelector("button[data-testid='enviar-acesso']");
    private final By ancoraMarcarHorario = By.cssSelector("a[data-testid='nav-marcar-horario']");


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

    public void entrar() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoEntrar)).click();

        wait.until(ExpectedConditions.elementToBeClickable(ancoraMarcarHorario)).click();
    }
}

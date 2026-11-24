package br.ifsp.demo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DetalheDoHorarioPage extends BasePage {
    private final By mensagemAviso = By.cssSelector("p.aviso[role='status']");
    private final By mensagemErro = By.cssSelector("p.aviso[role='alert']");
    private final By status = By.cssSelector("span.linha__status");

    public DetalheDoHorarioPage(WebDriver driver) {
        super(driver);
    }

    public String obterMensagemAviso() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemAviso)).getText();
    }

    public String obterMensagemErro() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemErro)).getText();
    }

    public String obterStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(status)).getText();
    }

    public DetalheDoHorarioPage avaliar(int nota) {
        By btnNota = By.cssSelector("button[aria-label='Dar nota " + nota + "']");

        wait.until(ExpectedConditions.elementToBeClickable(btnNota)).click();

        return this;
    }
}

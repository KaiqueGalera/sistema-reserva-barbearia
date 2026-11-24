package br.ifsp.demo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DetalheDoHorarioPage extends BasePage {
    private final By mensagemAviso = By.cssSelector("p.aviso[role='status']");
    private final By mensagemErro = By.cssSelector("p.aviso[role='alert']");
    private final By status = By.cssSelector("span.linha__status");
    private final By btnAbrirReagendamento = By.xpath("//button[text()='Escolher outro horário']");
    private final By btnConfirmarReagendamento = By.xpath("//button[text()='Mudar para este horário']");

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

    public DetalheDoHorarioPage reagendarHorario() {
        wait.until(ExpectedConditions.elementToBeClickable(btnAbrirReagendamento)).click();

        By botaoHorarioDisponivel = By.cssSelector(".seletor-horario button:not([disabled])");

        wait.until(ExpectedConditions.elementToBeClickable(botaoHorarioDisponivel)).click();
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmarReagendamento)).click();

        return this;
    }
}

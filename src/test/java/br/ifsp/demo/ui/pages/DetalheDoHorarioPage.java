package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DetalheDoHorarioPage extends PaginaBase {
    private final By mensagemAviso = By.cssSelector("p.aviso[role='status']");
    private final By mensagemErro = By.cssSelector("p.erro[role='alert']");
    private final By status = By.cssSelector("span.linha__status");

    private final By btnAbrirReagendamento = By.xpath("//button[text()='Escolher outro horário']");
    private final By btnConfirmarReagendamento = By.xpath("//button[text()='Mudar para este horário']");
    private final By btnAbrirCancelamento = By.xpath("//button[text()='Cancelar este horário']");
    private final By btnConfirmarCancelamento = By.xpath("//button[text()='Cancelar horário']");
    private final By btnConfirmarPresenca = By.xpath("//button[text()='Confirmar presença']");
    private final By btnManterHorario = By.xpath("//button[text()='Manter horário']");

    private final By voltar = By.cssSelector("a.voltar");

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

        By primeiroHorarioDisponivel = By.cssSelector(".seletor__horarios button:not([disabled])");

        wait.until(ExpectedConditions.elementToBeClickable(primeiroHorarioDisponivel)).click();
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmarReagendamento)).click();

        return this;
    }

    public DetalheDoHorarioPage cancelarHorario() {
        wait.until(ExpectedConditions.elementToBeClickable(btnAbrirCancelamento)).click();
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmarCancelamento)).click();

        return this;
    }

    public DetalheDoHorarioPage confirmarPresenca() {
        wait.until(ExpectedConditions.elementToBeClickable(btnConfirmarPresenca)).click();

        return this;
    }

    public MinhaAgendaPage voltarParaAgenda() {
        wait.until(ExpectedConditions.elementToBeClickable(voltar)).click();

        return new MinhaAgendaPage(driver);
    }

    public DetalheDoHorarioPage manterHorario() {
        wait.until(ExpectedConditions.elementToBeClickable(btnManterHorario))
                .click();

        return this;
    }
}

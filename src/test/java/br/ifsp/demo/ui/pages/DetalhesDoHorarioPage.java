package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlContains;

public class DetalhesDoHorarioPage extends PaginaBase {

    private static final By STATUS_DO_HORARIO = By.cssSelector(".detalhe__cabeca .linha__status");
    private static final By BOTAO_CANCELAR_PRIMEIRO = By.cssSelector(".botao-texto--perigo");
    private static final By BOTAO_CONFIRMAR_CANCELAMENTO = By.cssSelector(".bloco--perigo .botao--perigo");
    private static final By MENSAGEM_AVISO = By.cssSelector("[role='status']");
    private static final By VOLTAR = By.cssSelector("a.voltar");

    public DetalhesDoHorarioPage(WebDriver driver) {
        super(driver);
        espera.until(urlContains("/horarios/"));
        visivel(STATUS_DO_HORARIO);
    }

    public String statusDoHorario() {
        return visivel(STATUS_DO_HORARIO).getText();
    }

    public DetalhesDoHorarioPage solicitarCancelamento() {
        clicar(BOTAO_CANCELAR_PRIMEIRO);
        return this;
    }

    public DetalhesDoHorarioPage confirmarCancelamento() {
        clicar(BOTAO_CONFIRMAR_CANCELAMENTO);
        return this;
    }

    public String mensagemDeAviso() {
        return visivel(MENSAGEM_AVISO).getText();
    }

    public MinhaAgendaPage voltarParaAgenda() {
        clicar(VOLTAR);
        return new MinhaAgendaPage(driver);
    }
}
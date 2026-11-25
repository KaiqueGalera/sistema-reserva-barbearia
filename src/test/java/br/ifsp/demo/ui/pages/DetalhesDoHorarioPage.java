package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DetalhesDoHorarioPage extends PaginaBase{

    private static final By BOTAO_CANCELAR_PRIMEIRO = By.cssSelector(".botao-texto--perigo");
    private static final By BOTAO_CONFIRMAR_CANCELAMENTO = By.cssSelector(".bloco--perigo .botao--perigo");
    private static final By MENSAGEM_AVISO = By.cssSelector("[role='status']");

    public DetalhesDoHorarioPage(WebDriver driver) {
        super(driver);
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
}

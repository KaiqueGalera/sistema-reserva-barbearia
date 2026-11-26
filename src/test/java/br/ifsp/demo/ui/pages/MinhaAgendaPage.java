package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class MinhaAgendaPage extends PaginaBase {

    private static final By CONTEUDO_CARREGADO = By.cssSelector(".vazio, .agenda");
    private static final By AGENDA_COM_HORARIOS = By.cssSelector(".agenda");
    private static final By TITULO_VAZIO = By.cssSelector(".vazio h1");
    private static final By BOTAO_MARCAR_HORARIO = By.cssSelector(".vazio a.botao");

    private static final By PLACA = By.cssSelector("a.placa");
    private static final By PLACA_DIA = By.cssSelector("a.placa .placa__dia");
    private static final By PLACA_HORA = By.cssSelector("a.placa .placa__hora");
    private static final By PLACA_DETALHE = By.cssSelector("a.placa .placa__detalhe");
    private static final By PLACA_STATUS = By.cssSelector("a.placa .placa__status");

    private static final By LINHAS_DEPOIS_DESSE =
            By.xpath("//section[h2[normalize-space()='Depois desse']]//a[@class='linha']");
    private static final By LINHAS_HISTORICO =
            By.xpath("//section[h2[normalize-space()='Histórico']]//a[@class='linha']");
    private static final By STATUS_DA_LINHA = By.cssSelector(".linha__status");

    private static final By MENU_MARCAR_HORARIO = By.cssSelector("[data-testid='nav-marcar-horario']");
    private static final By BOTAO_SAIR = By.cssSelector(".topo__nav .botao-texto");


    public MinhaAgendaPage(WebDriver driver) {
        super(driver);
        garantirCaminho("/");
        visivel(CONTEUDO_CARREGADO);
    }

    public void recarregar() {
        driver.navigate().refresh();
        visivel(CONTEUDO_CARREGADO);
    }

    //estado geral
    public boolean estaVazia() {
        return driver.findElements(AGENDA_COM_HORARIOS).isEmpty();
    }

    public String tituloDaAgendaVazia() {
        return visivel(TITULO_VAZIO).getText();
    }

    //placa do próximo horário
    public boolean temProximoHorario() {
        return !driver.findElements(PLACA).isEmpty();
    }

    public String diaDoProximo() {
        return visivel(PLACA_DIA).getText();
    }

    public String horaDoProximo() {
        return visivel(PLACA_HORA).getText();
    }

    public String detalheDoProximo() {
        return visivel(PLACA_DETALHE).getText();
    }

    public String statusDoProximo() {
        return visivel(PLACA_STATUS).getText();
    }

    //listas
    public int quantidadeDepoisDesse() {
        return driver.findElements(LINHAS_DEPOIS_DESSE).size();
    }

    public int quantidadeNoHistorico() {
        return driver.findElements(LINHAS_HISTORICO).size();
    }

    public List<String> statusDasLinhasDoHistorico() {
        return driver.findElements(LINHAS_HISTORICO).stream()
                .map(linha -> linha.findElement(STATUS_DA_LINHA).getText())
                .toList();
    }

    //navegação
    public void abrirProximoHorario() {
        clicar(PLACA);
    }

    public void abrirLinhaDepoisDesse(int indice) {
        driver.findElements(LINHAS_DEPOIS_DESSE).get(indice).click();
    }

    public void clicarMarcarHorarioDaAgendaVazia() {
        clicar(BOTAO_MARCAR_HORARIO);
    }

    public void clicarMarcarHorarioNoMenu() {
        clicar(MENU_MARCAR_HORARIO);
    }

    public EntradaPage sair() {
        clicar(BOTAO_SAIR);
        return new EntradaPage(driver);
    }

}
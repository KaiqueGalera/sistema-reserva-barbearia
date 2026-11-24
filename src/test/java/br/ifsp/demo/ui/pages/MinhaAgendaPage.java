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

}
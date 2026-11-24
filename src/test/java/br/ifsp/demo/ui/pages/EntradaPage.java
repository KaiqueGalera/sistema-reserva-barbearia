package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class EntradaPage extends PaginaBase{
    private static final By ABA_CRIAR  = By.cssSelector("[data-testid='aba-criar-conta']");
    private static final By ENVIAR     = By.cssSelector("[data-testid='enviar-acesso']");
    private static final By NOME       = By.cssSelector("[autocomplete='given-name']");
    private static final By SOBRENOME  = By.cssSelector("[autocomplete='family-name']");
    private static final By EMAIL      = By.cssSelector("[autocomplete='email']");
    private static final By SENHA      = By.cssSelector("[autocomplete='new-password']");
    private static final By ERRO       = By.cssSelector("[role='alert']");
    private static final By AVISO      = By.cssSelector("[role='status']");

    public EntradaPage(WebDriver driver) { super(driver); }


    public EntradaPage abrirPag() {

        abrir("/entrar");  //abre o caminho .../entrar
        visivel(ENVIAR);  //ve se o botao de entrar esta visivel na tela
        return this;  //retorna o EntradaPage
    }


    public EntradaPage alternarParaCriarConta() {

        clicar(ABA_CRIAR); //clicar na aba criar
        return this;  //retorna
    }


    public EntradaPage preencherLogin(String email, String senha) {

        digitar(EMAIL, email);  //digita no campo "email" a string email
        digitar(SENHA, senha);
        return this;
    }

    public EntradaPage cadastrarConta(String nome, String sobrenome, String email, String senha) {

        digitar(NOME, nome);
        digitar(SOBRENOME, sobrenome);
        digitar(EMAIL, email);
        digitar(SENHA, senha);
        return this;
    }


    public EntradaPage enviar() {

        clicar(ENVIAR);
        return this;
    }


    public String mensagemDeErro() {
        return visivel(ERRO).getText();
    }

    public String mensagemDeAviso() {
        return visivel(AVISO).getText();
    }


    public EntradaPage abrirRotaProtegida(String rota) {
        abrir(rota);
        return this;
    }

    public void guardarTokenInvalido() {
        ((JavascriptExecutor) driver).executeScript(
                "localStorage.setItem('tres-cadeiras.token','token-invalido')");
    }

}

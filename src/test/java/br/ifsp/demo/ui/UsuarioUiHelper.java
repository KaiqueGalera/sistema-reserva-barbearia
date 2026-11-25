package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.EntradaPage;
import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.openqa.selenium.WebDriver;

import static br.ifsp.demo.ui.ConfiguracaoUi.FAKER;

public final class UsuarioUiHelper {

    public record Credenciais(String nome, String sobrenome, String email, String senha) {}

    private UsuarioUiHelper() {
    }

    public static Credenciais novasCredenciais() {
        return new Credenciais(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                "ui_" + System.nanoTime() + "@teste.com",
                FAKER.number().digits(6));
    }

    // Cria a conta pela UI e deixa o usuário logado na agenda
    public static Credenciais cadastrarNaUiMantendoLogado(WebDriver driver) {
        final Credenciais c = novasCredenciais();
        new EntradaPage(driver)
                .abrirPag()
                .alternarParaCriarConta()
                .cadastrarConta(c.nome(), c.sobrenome(), c.email(), c.senha())
                .enviar();
        new MinhaAgendaPage(driver);
        return c;
    }

    // Cria a conta pela UI e já sai (usuário fica deslogado)
    public static Credenciais cadastrarNaUi(WebDriver driver) {
        final Credenciais c = cadastrarNaUiMantendoLogado(driver);
        new MinhaAgendaPage(driver).sair();
        return c;
    }
}
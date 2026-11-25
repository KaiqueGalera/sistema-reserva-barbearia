package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.EntradaPage;
import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.openqa.selenium.WebDriver;

import static br.ifsp.demo.ui.ConfiguracaoUi.FAKER;

public final class UsuarioUiHelper {

    public record Credenciais(String nome, String sobrenome, String email, String senha) {}

    private UsuarioUiHelper() {
    }

    //cria a conta, espera carregar e clicar em sair antes de o teste fazer o login

    public static Credenciais cadastrarNaUi(WebDriver driver) {
        final Credenciais credenciais = new Credenciais(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                "ui_" + System.nanoTime() + "@teste.com",
                FAKER.number().digits(6));

        new EntradaPage(driver)
                .abrirPag()
                .alternarParaCriarConta()
                .cadastrarConta(credenciais.nome(), credenciais.sobrenome(),
                        credenciais.email(), credenciais.senha())
                .enviar();

        new MinhaAgendaPage(driver).sair();   // espera a agenda carregar e clica em Sair
        return credenciais;
    }
}
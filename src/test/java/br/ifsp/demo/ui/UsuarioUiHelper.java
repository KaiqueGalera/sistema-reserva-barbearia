package br.ifsp.demo.ui;

import br.ifsp.demo.security.auth.RegisterUserRequest;
import br.ifsp.demo.ui.pages.EntradaPage;
import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import io.restassured.http.ContentType;
import org.openqa.selenium.WebDriver;

import static br.ifsp.demo.ui.ConfiguracaoUi.FAKER;
import static br.ifsp.demo.ui.ConfiguracaoUi.URL_API;
import static io.restassured.RestAssured.given;

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


    // Cria a conta direto pela API (rápido, sem passar pela tela)
    public static Credenciais criarContaPelaApi() {
        final Credenciais c = novasCredenciais();
        given().baseUri(URL_API).contentType(ContentType.JSON)
                .body(new RegisterUserRequest(c.nome(), c.sobrenome(), c.email(), c.senha()))
                .when().post("/api/v1/register")
                .then().statusCode(201);
        return c;
    }

    // Faz login pela tela e devolve a agenda
    public static MinhaAgendaPage entrarPelaUi(WebDriver driver, Credenciais c) {
        new EntradaPage(driver)
                .abrirPag()
                .preencherLogin(c.email(), c.senha())
                .enviar();
        return new MinhaAgendaPage(driver);
    }

    // cria a conta pela API e agenda um corte às 10h para cada "daqui a N dias" informado
    static Credenciais contaComHorarios(int... diasAFrente) {

        final Credenciais credenciais = UsuarioUiHelper.criarContaPelaApi();
        final String token = ApiUiHelper.autenticar(credenciais);

        for (int dias : diasAFrente) {
            ApiUiHelper.agendarCorte(token, dias);
        }
        return credenciais;
    }
}
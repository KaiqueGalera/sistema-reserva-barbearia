package br.ifsp.demo.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.HttpURLConnection;
import java.net.URI;

import static br.ifsp.demo.ui.ConfiguracaoUi.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("UiTest")
public abstract class UiTestBase {

    protected WebDriver driver;

    @BeforeAll
    static void verificarAmbiente() {
        assumeTrue(respondeOk(URL_FRONT),
                "Front fora do ar em " + URL_FRONT + " (veja o README)");
        assumeTrue(respondeOk(URL_API + "/api/v1/openapi"),
                "Back-end fora do ar em " + URL_API + " (veja o README)");
    }

    @BeforeEach
    void abrirNavegador() {
        final ChromeOptions opcoes = new ChromeOptions();
        if (HEADLESS) {
            opcoes.addArguments("--headless=new");
        }
        opcoes.addArguments("--window-size=1366,768");
        driver = new ChromeDriver(opcoes);
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static boolean respondeOk(String url) {
        try {
            final HttpURLConnection conexao = (HttpURLConnection) URI.create(url).toURL().openConnection();
            conexao.setConnectTimeout(2000);
            conexao.setReadTimeout(2000);
            return conexao.getResponseCode() < 400;
        } catch (Exception e) {
            return false;
        }
    }

}

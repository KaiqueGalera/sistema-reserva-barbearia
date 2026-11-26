package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;

import static br.ifsp.demo.ui.ConfiguracaoUi.URL_FRONT;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;


public abstract class PaginaBase {

    protected final WebDriver driver;
    protected final WebDriverWait espera;

    protected PaginaBase(WebDriver driver) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    protected void abrir(String caminho) {
        driver.get(URL_FRONT + caminho);
    }

    protected WebElement visivel(By localizador) {
        return espera.until(visibilityOfElementLocated(localizador));
    }


    protected void digitar(By localizador, String texto) {
        final WebElement campo = visivel(localizador);
        campo.clear();
        campo.sendKeys(texto);
    }


    protected void clicar(By localizador) {
        espera.until(elementToBeClickable(localizador)).click();
    }


    public String caminhoAtual() {
        return URI.create(driver.getCurrentUrl()).getPath();
    }


    public void aguardarCaminho(String caminho) {
        espera.until(urlToBe(URL_FRONT + caminho));
    }

    public void aguardarCaminhoIniciadoPor(String prefixo) {
        espera.until(urlContains(URL_FRONT + prefixo));
    }

    protected void garantirCaminho(String esperado) {
        try {
            aguardarCaminho(esperado);
        } catch (TimeoutException e) {
            throw new IllegalStateException("Página errada: " + driver.getCurrentUrl());
        }
    }

}

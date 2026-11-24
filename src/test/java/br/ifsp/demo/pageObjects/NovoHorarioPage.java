package br.ifsp.demo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NovoHorarioPage extends BasePage {
    private final By nomeInput = By.cssSelector("input[autocomplete='name']");

    public NovoHorarioPage(WebDriver driver) {
        super(driver);
    }

    public NovoHorarioPage selecionarServico(String nomeServico) {
        By locator = By.cssSelector("[data-testid='servico-" + nomeServico + "'] input[type='checkbox']");
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(locator));

        if (!checkbox.isSelected()) {
            checkbox.click();
        }

        return this;
    }

    public NovoHorarioPage selecionarBarbeiro(String nomeBarbeiro) {
        By locator = By.cssSelector("[data-testid='barbeiro-" + nomeBarbeiro + "'] input[type = 'radio']");

        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

        return this;
    }

    public NovoHorarioPage selecionarHorarioDisponivel() {
        By botaoHorarioDisponivel = By.cssSelector(".seletor-horario button:not([disabled])");

        wait.until(ExpectedConditions.elementToBeClickable(botaoHorarioDisponivel)).click();

        return this;
    }

    public NovoHorarioPage preencherNome(String nome) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(nomeInput));

        input.clear();
        input.sendKeys(nome);

        return this;
    }
}

package br.ifsp.demo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;

public class NovoHorarioPage extends BasePage {
    private final By nomeInput = By.cssSelector("input[autocomplete='name']");
    private final By telefoneInput = By.cssSelector("input[autocomplete='tel-national']");
    private final By botaoEnviar = By.cssSelector("button[data-testid='enviar-agendamento']");
    private final By mensagemErro = By.cssSelector("p.erro[role='alert']");

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

    public NovoHorarioPage selecionarDia(LocalDate data) {
        By locator = By.cssSelector("[data-testid='dia-" + data + "']");

        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

        return this;
    }

    public NovoHorarioPage selecionarHorarioDisponivel() {
        By botaoHorarioDisponivel = By.cssSelector("button[data-testid^='horario-']:not([disabled])");

        wait.until(ExpectedConditions.elementToBeClickable(botaoHorarioDisponivel)).click();

        return this;
    }

    public NovoHorarioPage preencherNome(String nome) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(nomeInput));

        input.clear();
        input.sendKeys(nome);

        return this;
    }

    public NovoHorarioPage preencherTelefone(String telefone) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(telefoneInput));

        input.clear();
        input.sendKeys(telefone);

        return this;
    }

    public DetalheDoHorarioPage submeterAgendamentoComSucesso() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoEnviar)).click();

        return new DetalheDoHorarioPage(driver);
    }

    public NovoHorarioPage submeterAgendamentoComErro() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoEnviar)).click();

        return this;
    }

    public String obterMensagemErro() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemErro)).getText();
    }
}

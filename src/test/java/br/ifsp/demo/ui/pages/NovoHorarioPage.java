package br.ifsp.demo.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;

public class NovoHorarioPage extends PaginaBase {
    private final By nomeInput = By.cssSelector("input[autocomplete='name']");
    private final By telefoneInput = By.cssSelector("input[autocomplete='tel-national']");
    private final By botaoEnviar = By.cssSelector("button[data-testid='enviar-agendamento']");
    private final By mensagemErro = By.cssSelector("p.erro[role='alert']");
    private final By resumoHorario = By.xpath("//span[text()='Nenhum horário escolhido.']");

    public NovoHorarioPage(WebDriver driver) {
        super(driver);
    }

    public NovoHorarioPage selecionarServico(String nomeServico) {
        By checkbox = By.cssSelector(
                "[data-testid='servico-" + nomeServico + "'] input[type='checkbox']"
        );

        By opcaoServico = By.cssSelector(
                "label[data-testid='servico-" + nomeServico + "']"
        );

        WebElement elementoCheckbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(checkbox)
        );

        if (!elementoCheckbox.isSelected()) {
            wait.until(ExpectedConditions.elementToBeClickable(opcaoServico))
                    .click();
        }

        return this;
    }

    public NovoHorarioPage selecionarBarbeiro(String nomeBarbeiro) {
        By radio = By.cssSelector(
                "[data-testid='barbeiro-" + nomeBarbeiro + "'] input[type='radio']"
        );

        By opcaoBarbeiro = By.cssSelector(
                "label[data-testid='barbeiro-" + nomeBarbeiro + "']"
        );

        WebElement elementoRadio = wait.until(
                ExpectedConditions.presenceOfElementLocated(radio)
        );

        if (!elementoRadio.isSelected()) {
            wait.until(ExpectedConditions.elementToBeClickable(opcaoBarbeiro))
                    .click();
        }

        return this;
    }

    public NovoHorarioPage selecionarDiaDisponivel() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        String diaSelecionado = "dia-" + amanha.getYear() + "-" + amanha.getMonthValue() + "-" + amanha.getDayOfMonth();

        By radio = By.cssSelector("[data-testid='" + diaSelecionado + "'] input[type='radio']");

        By opcaoDia = By.cssSelector("label[data-testid='" + diaSelecionado + "']");

        WebElement elementoRadio = wait.until(
                ExpectedConditions.presenceOfElementLocated(radio)
        );

        if (!elementoRadio.isSelected()) {
            wait.until(ExpectedConditions.elementToBeClickable(opcaoDia))
                    .click();
        }

        return this;
    }

    public NovoHorarioPage selecionarHorarioDisponivel() {
        By primeiroHorarioDisponivel = By.cssSelector(
                ".seletor__horarios button:not([disabled])"
        );

        wait.until(ExpectedConditions.elementToBeClickable(primeiroHorarioDisponivel))
                .click();

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

    public String obterResumoHorario() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resumoHorario)).getText();
    }
}

package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.DetalheDoHorarioPage;
import br.ifsp.demo.ui.pages.LoginPage;
import br.ifsp.demo.ui.pages.NovoHorarioPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("UiTest")
public class AgendamentoUiTest {
    private static final String BASE_URL = "http://localhost:5173";

    private WebDriver driver;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        faker = Faker.instance();

        navegarParaMarcarHorario();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }


    private void navegarParaMarcarHorario() {
        driver.get(BASE_URL + "/entrar");

        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .preencherEmail("usuario.teste@gmail.com")
                .preencherSenha("cdd123fjk")
                .entrar();
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    @DisplayName("Deve criar agendamento com dados do Faker e navegar para a tela de detalhes")
    public void deveCriarAgendamentoComDadosDoFakerENavegarParaATelaDeDetalhes(int qtdDigitos) {
        String nome = faker.name().firstName() + " " + faker.name().lastName();
        String telefone = faker.number().digits(qtdDigitos);

        NovoHorarioPage novoPage = new NovoHorarioPage(driver);

        DetalheDoHorarioPage detalhePage = novoPage
                .selecionarServico("Barba")
                .selecionarBarbeiro("Neide")
                .selecionarDiaDisponivel()
                .selecionarHorarioDisponivel()
                .preencherNome(nome)
                .preencherTelefone(telefone)
                .submeterAgendamentoComSucesso();

        assertThat(detalhePage.obterMensagemAviso()).isEqualTo("Horário marcado.");
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 12, 13})
    @DisplayName("Deve exibir erro de validação ao informar telefone com uma quantidade de digitos incorreta")
    public void deveExibirErroDeValidacaoAoInformarTelefoneComUmaQuantidadeDeDigitosIncorreta(int qtdDigitos) {
        String nome = faker.name().firstName() + " " + faker.name().lastName();
        String telefone = faker.number().digits(qtdDigitos);

        NovoHorarioPage novoPage = new NovoHorarioPage(driver);

        novoPage
                .selecionarServico("Barba")
                .selecionarBarbeiro("Neide")
                .selecionarDiaDisponivel()
                .selecionarHorarioDisponivel()
                .preencherNome(nome)
                .preencherTelefone(telefone)
                .submeterAgendamentoComSucesso();

        assertThat(novoPage.obterMensagemErro())
                .isEqualTo("Informe o telefone com DDD, com 10 ou 11 dígitos.");
    }
}

package br.ifsp.demo.ui;

import br.ifsp.demo.pageObjects.DetalheDoHorarioPage;
import br.ifsp.demo.pageObjects.LoginPage;
import br.ifsp.demo.pageObjects.NovoHorarioPage;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
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

    @Test
    @DisplayName("Deve criar agendamento com dados do Faker e navegar para a tela de detalhes")
    public void deveCriarAgendamentoComDadosDoFakerENavegarParaATelaDeDetalhes() {
        driver.get(BASE_URL + "/novo");

        String nome = faker.name().fullName();
        String telefone = faker.phoneNumber().cellPhone();

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
}

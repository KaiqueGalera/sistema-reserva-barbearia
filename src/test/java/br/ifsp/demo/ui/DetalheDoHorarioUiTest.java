package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("UiTest")
public class DetalheDoHorarioUiTest {
    private static final String BASE_URL = "http://localhost:5173";

    private WebDriver driver;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        faker = Faker.instance();

        navegarParaDetalhes();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private void navegarParaDetalhes() {
        driver.get(BASE_URL + "/entrar");

        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .preencherEmail("usuario.teste@gmail.com")
                .preencherSenha("cdd123fjk")
                .entrar();

        String nome = faker.name().fullName();
        String telefone = faker.number().digits(11);

        NovoHorarioPage novoPage = new NovoHorarioPage(driver);

        novoPage
                .selecionarServico("Barba")
                .selecionarBarbeiro("Neide")
                .selecionarDiaDisponivel()
                .selecionarHorarioDisponivel()
                .preencherNome(nome)
                .preencherTelefone(telefone)
                .submeterAgendamentoComSucesso();
    }

    @Test
    @DisplayName("Deve confirmar presença no agendamento")
    public void deveConfirmarPresencaNoAgendamento() {
        DetalheDoHorarioPage detalhePage = new DetalheDoHorarioPage(driver);

        detalhePage.confirmarPresenca();

        assertThat(detalhePage.obterMensagemAviso())
                .isEqualTo("Presença confirmada.");
    }

    @Test
    @DisplayName("Deve cancelar agendamento com sucesso")
    public void deveCancelarAgendamentoComSucesso() {
        DetalheDoHorarioPage detalhePage = new DetalheDoHorarioPage(driver);

        detalhePage.cancelarHorario();

        assertThat(detalhePage.obterMensagemAviso())
                .isEqualTo("Horário cancelado.");
    }

    @Test
    @DisplayName("Deve manter agendamento ao desistir do cancelamento")
    public void deveManterAgendamentoAoDesistirDoCancelamento() {
        DetalheDoHorarioPage detalhePage = new DetalheDoHorarioPage(driver);

        detalhePage.cancelarHorario();
        detalhePage.manterHorario();

        assertThat(detalhePage.obterStatus())
                .isNotEqualTo("Horário cancelado.");
    }

    @Test
    @DisplayName("Deve reagendar horário com sucesso")
    public void deveReagendarHorarioComSucesso() {
        DetalheDoHorarioPage detalhePage = new DetalheDoHorarioPage(driver);

        detalhePage.reagendarHorario();

        assertThat(detalhePage.obterMensagemAviso())
                .isEqualTo("Horário alterado.");
    }
}
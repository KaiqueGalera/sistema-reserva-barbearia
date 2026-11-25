package br.ifsp.demo.ui;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("Deve criar agendamento com dados do Faker e navegar para a tela de detalhes")
    public void deveCriarAgendamentoComDadosDoFakerENavegarParaATelaDeDetalhes() {
        driver.get(BASE_URL + "/novo");

        String nome = faker.name().fullName();
    }
}

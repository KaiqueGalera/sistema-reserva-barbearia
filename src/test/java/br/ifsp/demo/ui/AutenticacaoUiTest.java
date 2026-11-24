package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.EntradaPage;
import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class AutenticacaoUiTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Deve realizar login com credenciais válidas e carregar a agenda")
    void deveRealizarLoginComCredenciaisValidas() {

        EntradaPage entradaPage = new EntradaPage(driver);

        //criar conta:
        entradaPage.abrirPag()
                .alternarParaCriarConta()
                .cadastrarConta("Fulana", "Silva", "fulanalinda@gmail.com", "1234")
                .enviar();

        //entrar com essa conta:
        entradaPage.abrirPag()
                .preencherLogin("fulanalinda@gmail.com", "1234")
                .enviar();

        MinhaAgendaPage agendaPage = new MinhaAgendaPage(driver);

        assertThat(agendaPage.caminhoAtual()).isEqualTo("/");
    }

}

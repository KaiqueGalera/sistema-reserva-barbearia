package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("UiTest")
public class MinhaAgendaUiTest extends UiTestBase {

    @Test
    @DisplayName("Deve mostrar a agenda vazia para um usuário sem horários")
    void deveMostrarAgendaVaziaParaUsuarioNovo() {

        //cria um user e ja deixa ele logado
        UsuarioUiHelper.cadastrarNaUiMantendoLogado(driver);

        //cria a agenda
        final MinhaAgendaPage agenda = new MinhaAgendaPage(driver);

        assertThat(agenda.estaVazia()).isTrue();
        assertThat(agenda.tituloDaAgendaVazia()).isEqualTo("Sua agenda está livre");
    }



    @Test
    @DisplayName("Deve ir para novo horário pelo botão da agenda vazia")
    void deveIrParaNovoHorarioPeloBotaoDaAgendaVazia() {

        UsuarioUiHelper.cadastrarNaUiMantendoLogado(driver);

        final MinhaAgendaPage agenda = new MinhaAgendaPage(driver);

        agenda.clicarMarcarHorarioDaAgendaVazia();
        agenda.aguardarCaminho("/novo");

        assertThat(agenda.caminhoAtual()).isEqualTo("/novo");
    }
}

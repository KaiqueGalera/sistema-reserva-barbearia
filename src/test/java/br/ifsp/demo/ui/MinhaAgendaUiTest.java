package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static br.ifsp.demo.ui.UsuarioUiHelper.contaComHorarios;
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


    @Test
    @DisplayName("Deve ir para novo horário pelo menu")
    void deveIrParaNovoHorarioPeloMenu() {

        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, UsuarioUiHelper.criarContaPelaApi());

        agenda.clicarMarcarHorarioNoMenu();

        agenda.aguardarCaminho("/novo");
        assertThat(agenda.caminhoAtual()).isEqualTo("/novo");
    }


    @Test
    @DisplayName("Deve mostrar a placa do próximo horário com os dados do agendamento")
    void deveMostrarPlacaDoProximoHorario() {

        final UsuarioUiHelper.Credenciais credenciais = contaComHorarios(2);

        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, credenciais);

        assertThat(agenda.temProximoHorario()).isTrue();
        assertThat(agenda.diaDoProximo()).contains("Seu próximo horário é");
        assertThat(agenda.horaDoProximo()).contains("10");
        assertThat(agenda.detalheDoProximo()).contains("Corte");
        assertThat(agenda.statusDoProximo()).isEqualTo("Falta confirmar presença");
    }


    @Test
    @DisplayName("Deve listar os demais horários futuros em 'Depois desse'")
    void deveListarHorariosSeguintesEmDepoisDesse() {

        final UsuarioUiHelper.Credenciais credenciais = contaComHorarios(10, 11, 12);

        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, credenciais);

        assertThat(agenda.temProximoHorario()).isTrue();
        assertThat(agenda.quantidadeDepoisDesse()).isEqualTo(2);
        assertThat(agenda.quantidadeNoHistorico()).isZero();
    }


}

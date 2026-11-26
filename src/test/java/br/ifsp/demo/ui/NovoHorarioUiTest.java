package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import br.ifsp.demo.ui.pages.NovoHorarioPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class NovoHorarioUiTest extends UiTestBase {

    @Test
    @DisplayName("Deve manter o botão de marcar desabilitado enquanto nenhum horário for escolhido")
    void deveManterOBotaoDeMarcarDesabilitadoEnquantoNenhumHorarioForEscolhido() {
        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, UsuarioUiHelper.criarContaPelaApi());
        agenda.clicarMarcarHorarioNoMenu();
        agenda.aguardarCaminho("/novo");

        final NovoHorarioPage novoHorario = new NovoHorarioPage(driver);

        assertThat(novoHorario.botaoDeEnvioEstaHabilitado()).isFalse();
        assertThat(novoHorario.obterResumo()).isEqualTo("Nenhum horário escolhido.");

        novoHorario.selecionarDiaDisponivel().selecionarHorarioDisponivel();

        assertThat(novoHorario.botaoDeEnvioEstaHabilitado()).isTrue();
    }

    @Test
    @DisplayName("Deve pedir ao menos um serviço quando nenhum estiver marcado")
    void devePedirAoMenosUmServicoQuandoNenhumEstiverMarcado() {
        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, UsuarioUiHelper.criarContaPelaApi());
        agenda.clicarMarcarHorarioNoMenu();
        agenda.aguardarCaminho("/novo");

        final NovoHorarioPage novoHorario = new NovoHorarioPage(driver).desmarcarServico("Corte");

        assertThat(novoHorario.obterAvisoDeHorarios())
                .isEqualTo("Escolha ao menos um serviço para ver os horários livres.");
        assertThat(novoHorario.botaoDeEnvioEstaHabilitado()).isFalse();
    }

    @Test
    @DisplayName("Deve manter a tela de novo horário utilizável em tela de celular")
    void deveManterATelaDeNovoHorarioUtilizavelEmTelaDeCelular() {
        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, UsuarioUiHelper.criarContaPelaApi());
        agenda.clicarMarcarHorarioNoMenu();
        agenda.aguardarCaminho("/novo");

        driver.manage().window().setSize(new Dimension(375, 812));

        final NovoHorarioPage novoHorario = new NovoHorarioPage(driver);

        assertThat(novoHorario.obterTitulo()).isEqualTo("Marcar horário");
        assertThat(novoHorario.botaoDeEnvioEstaVisivel()).isTrue();
        assertThat(novoHorario.temRolagemHorizontal()).isFalse();
    }
}

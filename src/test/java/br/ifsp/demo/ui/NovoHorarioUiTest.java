package br.ifsp.demo.ui;

import br.ifsp.demo.pageObjects.NovoHorarioPage;
import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.ifsp.demo.ui.ConfiguracaoUi.FAKER;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class NovoHorarioUiTest extends UiTestBase {

    @Test
    @DisplayName("Deve mostrar erro ao enviar o telefone com poucos digitos")
    void deveMostrarErroAoEnviarOTelefoneComPoucosDigitos() {
        final MinhaAgendaPage agenda = UsuarioUiHelper.entrarPelaUi(driver, UsuarioUiHelper.criarContaPelaApi());
        agenda.clicarMarcarHorarioNoMenu();
        agenda.aguardarCaminho("/novo");

        final NovoHorarioPage novoHorario = new NovoHorarioPage(driver)
                .selecionarDia(LocalDate.now().plusDays(1))
                .selecionarHorarioDisponivel()
                .preencherNome(FAKER.name().fullName())
                .preencherTelefone("123")
                .submeterAgendamentoComErro();

        assertThat(novoHorario.obterMensagemErro()).isEqualTo("Informe o telefone com DDD, com 10 ou 11 digitos.");
        assertThat(agenda.caminhoAtual()).isEqualTo("/novo");
    }
}

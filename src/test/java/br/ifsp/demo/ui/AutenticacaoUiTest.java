package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.EntradaPage;
import br.ifsp.demo.ui.pages.MinhaAgendaPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class AutenticacaoUiTest extends UiTestBase {

    @Test
    @DisplayName("Deve realizar login com credenciais válidas e carregar a agenda")
    void deveRealizarLoginComCredenciaisValidas() {

        //cadastra a conta pelo UsuarioUiHelper
        final var credenciais = UsuarioUiHelper.cadastrarNaUi(driver);

        //entra com essa conta
        new EntradaPage(driver)
                .abrirPag()
                .preencherLogin(credenciais.email(), credenciais.senha())
                .enviar();

        final MinhaAgendaPage agendaPage = new MinhaAgendaPage(driver);

        //ve se carregou a agenda
        assertThat(agendaPage.caminhoAtual()).isEqualTo("/");
    }

    @Test
    @DisplayName("Deve mostrar erro e permanecer em /entrar com e-mail não cadastradoe não sair da página")
    void deveMostrarErroComEmailNaoCadastradoENaoSairDaPagina() {

        //cadastra a conta pelo UsuarioUiHelper
        final var credenciais = UsuarioUiHelper.cadastrarNaUi(driver);

        //entra com essa conta, com os dados errados
        final EntradaPage entrada = new EntradaPage(driver)
                .abrirPag()
                .preencherLogin("email-errado@gmail.com", credenciais.senha())
                .enviar();

        assertThat(entrada.mensagemDeErro()).contains("E-mail ou senha incorretos");
        assertThat(entrada.caminhoAtual()).isEqualTo("/entrar");

    }

    @Test
    @DisplayName("Deve mostrar erro e permanecer em /entrar com senha incorreta")
    void deveMostrarErroComSenhaIncorreta() {

        final var credenciais = UsuarioUiHelper.cadastrarNaUi(driver);

        final EntradaPage entrada = new EntradaPage(driver)
                .abrirPag()
                .preencherLogin(credenciais.email(), "senha-errada")
                .enviar();

        assertThat(entrada.mensagemDeErro()).contains("E-mail ou senha incorretos");
        assertThat(entrada.caminhoAtual()).isEqualTo("/entrar");
    }


    @Test
    @DisplayName("Deve mostrar erro ao criar conta com e-mail já cadastrado")
    void deveMostrarErroAoCadastrarEmailJaExistente() {

        final var existente = UsuarioUiHelper.cadastrarNaUi(driver);

        final EntradaPage entrada = new EntradaPage(driver)
                .abrirPag()
                .alternarParaCriarConta()
                .cadastrarConta(existente.nome(), existente.sobrenome(), existente.email(), existente.senha())
                .enviar();

        assertThat(entrada.mensagemDeErro()).contains("já tem conta");
        assertThat(entrada.caminhoAtual()).isEqualTo("/entrar");
    }


    @Test
    @DisplayName("Deve alternar entre as abas Entrar e Criar conta mostrando os campos certos")
    void deveAlternarEntreAbasMostrandoOsCamposCertos() {

        final EntradaPage entrada = new EntradaPage(driver).abrirPag();

        assertThat(entrada.campoDeNomeAusente()).isTrue();

        entrada.alternarParaCriarConta();

        assertThat(entrada.campoDeNomeVisivel()).isTrue();
        assertThat(entrada.campoDeSobrenomeVisivel()).isTrue();

        entrada.alternarParaEntrar();

        assertThat(entrada.campoDeNomeAusente()).isTrue();
    }








}

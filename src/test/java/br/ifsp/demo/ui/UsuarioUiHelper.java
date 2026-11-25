package br.ifsp.demo.ui;

import br.ifsp.demo.ui.pages.EntradaPage;

import static br.ifsp.demo.ui.ConfiguracaoUi.FAKER;

public class UsuarioUiHelper{

    private static String ultimoNome;
    private static String ultimoSobrenome;
    private static String ultimoEmail;
    private static String ultimaSenha;

    public static void cadastrarNaUi(EntradaPage entradaPage) {
        ultimoNome = FAKER.name().firstName();
        ultimoSobrenome = FAKER.name().lastName();
        ultimoEmail = "123_" + FAKER.internet().emailAddress();
        ultimaSenha = FAKER.number().digits(6);

        entradaPage.abrirPag()
                .alternarParaCriarConta()
                .cadastrarConta(ultimoNome, ultimoSobrenome, ultimoEmail, ultimaSenha)
                .enviar();
    }
}

package br.ifsp.demo.api;

import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.empty;

class AgendamentoApiTest extends ApiTestBase {

    @Test
    @DisplayName("Deve retornar 401 ao listar agendamentos sem token")
    void deveRetornar401AoListarAgendamentosSemToken() {
        requisicao()
                .when()
                .get("/api/v1/agendamentos")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Deve retornar 200 e lista vazia para cliente sem agendamento")
    void deveRetornar200EListaVaziaParaClienteSemAgendamento() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos")
                .then()
                .statusCode(200)
                .body("$", empty());
    }
}

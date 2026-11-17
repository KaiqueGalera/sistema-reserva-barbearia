package br.ifsp.demo.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}

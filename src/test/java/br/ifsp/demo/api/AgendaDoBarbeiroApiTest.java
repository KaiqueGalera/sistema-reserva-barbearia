package br.ifsp.demo.api;

import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class AgendaDoBarbeiroApiTest extends ApiTestBase {

    @Test
    @DisplayName("Deve listar horários ocupados vazios para barbeiro autenticado")
    void deveListarHorariosOcupados() {
        String senha = "senha123";
        User usuario = registrarUsuario(senha);
        String token = autenticar(usuario.getUsername(), senha);
        UUID barbeiroId = UUID.randomUUID();

        requisicaoAutenticada(token)
                .queryParam("data", "2030-06-10")
                .when()
                .get("/api/v1/barbeiros/{barbeiroId}/horarios-ocupados", barbeiroId)
                .then()
                .statusCode(200)
                .body("$", org.hamcrest.Matchers.empty());
    }

    @Test
    @DisplayName("Deve listar horários disponíveis vazios para dados válidos")
    void deveListarHorariosDisponiveis() {
        String senha = "senha123";
        User usuario = registrarUsuario(senha);
        String token = autenticar(usuario.getUsername(), senha);
        UUID barbeiroId = UUID.randomUUID();

        requisicaoAutenticada(token)
                .queryParam("data", "2030-06-10")
                .queryParam("servicos", "11111111-1111-1111-1111-111111111111")
                .when()
                .get("/api/v1/barbeiros/{barbeiroId}/horarios-disponiveis", barbeiroId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Deve retornar 401 ao consultar agenda do barbeiro sem token")
    void deveRetornar401NaAgendaSemToken() {
        requisicao()
                .queryParam("data", "2030-06-10")
                .when()
                .get("/api/v1/barbeiros/{barbeiroId}/horarios-ocupados", UUID.randomUUID())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Deve rejeitar consulta de disponibilidade para data passada")
    void deveRejeitarConsultaDeDisponibilidadeParaDataPassada() {
        String senha = "senha123";
        User usuario = registrarUsuario(senha);
        String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .queryParam("data", "2020-06-10")
                .queryParam("servicos", "11111111-1111-1111-1111-111111111111")
                .when()
                .get("/api/v1/barbeiros/{barbeiroId}/horarios-disponiveis", UUID.randomUUID())
                .then()
                .statusCode(422);
    }
}

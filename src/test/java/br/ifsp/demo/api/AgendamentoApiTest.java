package br.ifsp.demo.api;

import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.ifsp.demo.api.AgendamentoRequestBuilder.SERVICO_CORTE;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.amanhaAs;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.formatar;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.umAgendamento;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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

    @Test
    @DisplayName("Deve retornar 201 e o agendamento criado ao agendar com dados válidos")
    void deveRetornar201EOAgendamentoCriadoAoAgendarComDadosValidos() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final UUID barbeiroId = UUID.randomUUID();
        final LocalDateTime inicio = amanhaAs(10);

        requisicaoAutenticada(token)
                .body(umAgendamento()
                        .comBarbeiro(barbeiroId)
                        .comContato("Kaique Galera", "16999998888")
                        .comInicio(inicio)
                        .comServicos(SERVICO_CORTE)
                        .construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("barbeiroId", equalTo(barbeiroId.toString()))
                .body("nomeDoContato", equalTo("Kaique Galera"))
                .body("telefoneDoContato", equalTo("16999998888"))
                .body("inicio", equalTo(formatar(inicio)))
                .body("fim", equalTo(formatar(inicio.plusMinutes(30))))
                .body("status", equalTo("AGENDADO"))
                .body("valorTotal", comparesEqualTo(40.0f))
                .body("duracaoTotalEmMinutos", equalTo(30))
                .body("quantidadeDeReagendamentos", equalTo(0))
                .body("itens", hasSize(1))
                .body("itens[0].servicoId", equalTo(SERVICO_CORTE.toString()))
                .body("itens[0].nomeDoServico", equalTo("Corte"))
                .body("avaliacao", nullValue());
    }

    @Test
    @DisplayName("Deve retornar 401 ao agendar sem token")
    void deveRetornar401AoAgendarSemToken() {
        requisicao()
                .body(umAgendamento().construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Deve retornar 400 ao agendar sem informar os serviços")
    void deveRetornar400AoAgendarSemInformarOsServicos() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(umAgendamento().semServicos().construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(400)
                .body("status", equalTo("BAD_REQUEST"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 400 ao agendar sem informar o início")
    void deveRetornar400AoAgendarSemInformarOInicio() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(umAgendamento().semInicio().construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(400)
                .body("status", equalTo("BAD_REQUEST"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 400 ao agendar com JSON malformado")
    void deveRetornar400AoAgendarComJsonMalformado() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body("{ \"barbeiroId\": ")
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Deve retornar 404 ao agendar um serviço fora do catálogo")
    void deveRetornar404AoAgendarUmServicoForaDoCatalogo() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(umAgendamento().comServicos(UUID.randomUUID()).construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }
}

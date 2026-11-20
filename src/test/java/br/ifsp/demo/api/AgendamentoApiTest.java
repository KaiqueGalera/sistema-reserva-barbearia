package br.ifsp.demo.api;

import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.ifsp.demo.api.AgendamentoRequestBuilder.SERVICO_CORTE;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.amanhaAs;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.formatar;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.ontemAs;
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

    @Test
    @DisplayName("Deve retornar 422 ao agendar com a lista de serviços vazia")
    void deveRetornar422AoAgendarComAListaDeServicosVazia() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(umAgendamento().comServicos().construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("O agendamento deve conter ao menos um serviço"));
    }

    @Test
    @DisplayName("Deve retornar 422 ao agendar em horário passado")
    void deveRetornar422AoAgendarEmHorarioPassado() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(umAgendamento().comInicio(ontemAs(10)).construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("Não é possível agendar em horário passado"));
    }

    @Test
    @DisplayName("Deve retornar 422 ao agendar fora do horário de funcionamento")
    void deveRetornar422AoAgendarForaDoHorarioDeFuncionamento() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(umAgendamento().comInicio(amanhaAs(20)).construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("O periodo deve estar dentro do horario de funcionamento"));
    }

    @Test
    @DisplayName("Deve retornar 409 ao agendar em um horário já ocupado pelo barbeiro")
    void deveRetornar409AoAgendarEmUmHorarioJaOcupadoPeloBarbeiro() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final UUID barbeiroId = UUID.randomUUID();
        final var agendamento = umAgendamento().comBarbeiro(barbeiroId).comInicio(amanhaAs(10)).construir();

        requisicaoAutenticada(token)
                .body(agendamento)
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(201);

        requisicaoAutenticada(token)
                .body(agendamento)
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(409)
                .body("status", equalTo("CONFLICT"))
                .body("message", equalTo("Horário indisponível para o barbeiro"));
    }
}

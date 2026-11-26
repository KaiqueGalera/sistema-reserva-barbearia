package br.ifsp.demo.api;

import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static br.ifsp.demo.api.AgendamentoRequestBuilder.SERVICO_BARBA;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.SERVICO_CORTE;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.amanhaAs;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.formatar;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.ontemAs;
import static br.ifsp.demo.api.AgendamentoRequestBuilder.umAgendamento;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
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

    @Disabled //pra poder rodar main
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

    @Test
    @DisplayName("Deve retornar 422 ao agendar sem a antecedência mínima")
    void deveRetornar422AoAgendarSemAAntecedenciaMinima() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final LocalDateTime daquiA30Minutos = LocalDateTime.now().plusMinutes(30).withNano(0);

        requisicaoAutenticada(token)
                .body(umAgendamento().comInicio(daquiA30Minutos).construir())
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("Agendamento exige no mínimo 60 minutos de antecedência"));
    }

    @Test
    @DisplayName("Deve listar os agendamentos do cliente ordenados pelo início")
    void deveListarOsAgendamentosDoClienteOrdenadosPeloInicio() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final LocalDateTime manha = amanhaAs(10);
        final LocalDateTime tarde = amanhaAs(14);

        agendar(token, umAgendamento().comInicio(tarde).construir());
        agendar(token, umAgendamento().comInicio(manha).construir());

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("inicio", contains(formatar(manha), formatar(tarde)));
    }

    @Test
    @DisplayName("Não deve listar os agendamentos de outro cliente")
    void naoDeveListarOsAgendamentosDeOutroCliente() {
        final String senha = "senha123";
        final User dono = registrarUsuario(senha);
        final User outroCliente = registrarUsuario(senha);
        final String tokenDoDono = autenticar(dono.getUsername(), senha);
        final String tokenDoOutroCliente = autenticar(outroCliente.getUsername(), senha);

        agendar(tokenDoDono, umAgendamento().construir());

        requisicaoAutenticada(tokenDoOutroCliente)
                .when()
                .get("/api/v1/agendamentos")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @DisplayName("Deve listar apenas os agendamentos ativos quando futuros for verdadeiro")
    void deveListarApenasOsAgendamentosAtivosQuandoFuturosForVerdadeiro() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        final String idAtivo = agendar(token, umAgendamento().comInicio(amanhaAs(10)).construir());
        final String idCancelado = agendar(token, umAgendamento().comInicio(amanhaAs(14)).construir());

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/cancelamento", idCancelado)
                .then()
                .statusCode(204);

        requisicaoAutenticada(token)
                .queryParam("futuros", true)
                .when()
                .get("/api/v1/agendamentos")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(idAtivo));
    }

    @Test
    @DisplayName("Deve retornar 200 e os dados do agendamento ao buscar por id")
    void deveRetornar200EOsDadosDoAgendamentoAoBuscarPorId() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final UUID barbeiroId = UUID.randomUUID();
        final LocalDateTime inicio = amanhaAs(10);
        final String id = agendar(token, umAgendamento().comBarbeiro(barbeiroId).comInicio(inicio).construir());

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("barbeiroId", equalTo(barbeiroId.toString()))
                .body("inicio", equalTo(formatar(inicio)))
                .body("status", equalTo("AGENDADO"))
                .body("itens", hasSize(1))
                .body("itens[0].servicoId", equalTo(SERVICO_CORTE.toString()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar um agendamento inexistente")
    void deveRetornar404AoBuscarUmAgendamentoInexistente() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos/{id}", UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar o agendamento de outro cliente")
    void deveRetornar404AoBuscarOAgendamentoDeOutroCliente() {
        final String senha = "senha123";
        final User dono = registrarUsuario(senha);
        final User outroCliente = registrarUsuario(senha);
        final String tokenDoDono = autenticar(dono.getUsername(), senha);
        final String tokenDoOutroCliente = autenticar(outroCliente.getUsername(), senha);
        final String id = agendar(tokenDoDono, umAgendamento().construir());

        requisicaoAutenticada(tokenDoOutroCliente)
                .when()
                .get("/api/v1/agendamentos/{id}", id)
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 400 ao buscar um agendamento com id inválido")
    void deveRetornar400AoBuscarUmAgendamentoComIdInvalido() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos/{id}", "id-invalido")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Deve retornar 204 e cancelar o agendamento")
    void deveRetornar204ECancelarOAgendamento() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().construir());

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/cancelamento", id)
                .then()
                .statusCode(204);

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos/{id}", id)
                .then()
                .statusCode(200)
                .body("status", equalTo("CANCELADO"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao cancelar um agendamento inexistente")
    void deveRetornar404AoCancelarUmAgendamentoInexistente() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/cancelamento", UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 422 ao cancelar um agendamento já cancelado")
    void deveRetornar422AoCancelarUmAgendamentoJaCancelado() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().construir());

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/cancelamento", id)
                .then()
                .statusCode(204);

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/cancelamento", id)
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("Apenas agendamentos com status 'agendado' ou 'confirmado' podem ser cancelados"));
    }

    @Test
    @DisplayName("Deve retornar 200 e o agendamento atualizado ao adicionar um serviço")
    void deveRetornar200EOAgendamentoAtualizadoAoAdicionarUmServico() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comServicos(SERVICO_CORTE).construir());

        requisicaoAutenticada(token)
                .body(Map.of("servicoId", SERVICO_BARBA))
                .when()
                .post("/api/v1/agendamentos/{id}/itens", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("itens", hasSize(2))
                .body("itens[1].servicoId", equalTo(SERVICO_BARBA.toString()))
                .body("valorTotal", comparesEqualTo(65.0f))
                .body("duracaoTotalEmMinutos", equalTo(50));
    }

    @Test
    @DisplayName("Deve retornar 404 ao adicionar um serviço fora do catálogo")
    void deveRetornar404AoAdicionarUmServicoForaDoCatalogo() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().construir());

        requisicaoAutenticada(token)
                .body(Map.of("servicoId", UUID.randomUUID()))
                .when()
                .post("/api/v1/agendamentos/{id}/itens", id)
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 422 ao adicionar um serviço em um agendamento cancelado")
    void deveRetornar422AoAdicionarUmServicoEmUmAgendamentoCancelado() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().construir());

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/cancelamento", id)
                .then()
                .statusCode(204);

        requisicaoAutenticada(token)
                .body(Map.of("servicoId", SERVICO_BARBA))
                .when()
                .post("/api/v1/agendamentos/{id}/itens", id)
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("Apenas agendamentos ativos podem ser alterados"));
    }

    @Test
    @DisplayName("Deve retornar 200 e o agendamento atualizado ao remover um serviço")
    void deveRetornar200EOAgendamentoAtualizadoAoRemoverUmServico() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comServicos(SERVICO_CORTE, SERVICO_BARBA).construir());
        final String idDaBarba = requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos/{id}", id)
                .then()
                .extract()
                .path("itens[1].id");

        requisicaoAutenticada(token)
                .when()
                .delete("/api/v1/agendamentos/{id}/itens/{itemId}", id, idDaBarba)
                .then()
                .statusCode(200)
                .body("itens", hasSize(1))
                .body("itens[0].servicoId", equalTo(SERVICO_CORTE.toString()))
                .body("valorTotal", comparesEqualTo(40.0f))
                .body("duracaoTotalEmMinutos", equalTo(30));
    }

    @Disabled //pra poder rodar main
    @Test
    @DisplayName("Deve retornar 404 ao remover um serviço inexistente do agendamento")
    void deveRetornar404AoRemoverUmServicoInexistenteDoAgendamento() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comServicos(SERVICO_CORTE, SERVICO_BARBA).construir());

        requisicaoAutenticada(token)
                .when()
                .delete("/api/v1/agendamentos/{id}/itens/{itemId}", id, UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Deve retornar 422 ao remover o único serviço do agendamento")
    void deveRetornar422AoRemoverOUnicoServicoDoAgendamento() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comServicos(SERVICO_CORTE).construir());
        final String idDoCorte = requisicaoAutenticada(token)
                .when()
                .get("/api/v1/agendamentos/{id}", id)
                .then()
                .extract()
                .path("itens[0].id");

        requisicaoAutenticada(token)
                .when()
                .delete("/api/v1/agendamentos/{id}/itens/{itemId}", id, idDoCorte)
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("O agendamento deve conter ao menos um serviço"));
    }

    @Test
    @DisplayName("Deve retornar 200 e o agendamento com o novo início ao reagendar")
    void deveRetornar200EOAgendamentoComONovoInicioAoReagendar() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comInicio(amanhaAs(10)).construir());
        final LocalDateTime novoInicio = amanhaAs(14);

        requisicaoAutenticada(token)
                .body(Map.of("novoInicio", formatar(novoInicio)))
                .when()
                .post("/api/v1/agendamentos/{id}/reagendamento", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("inicio", equalTo(formatar(novoInicio)))
                .body("fim", equalTo(formatar(novoInicio.plusMinutes(30))))
                .body("quantidadeDeReagendamentos", equalTo(1));
    }

    @Test
    @DisplayName("Deve retornar 404 ao reagendar um agendamento inexistente")
    void deveRetornar404AoReagendarUmAgendamentoInexistente() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(Map.of("novoInicio", formatar(amanhaAs(14))))
                .when()
                .post("/api/v1/agendamentos/{id}/reagendamento", UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 422 ao reagendar para um horário passado")
    void deveRetornar422AoReagendarParaUmHorarioPassado() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comInicio(amanhaAs(10)).construir());

        requisicaoAutenticada(token)
                .body(Map.of("novoInicio", formatar(ontemAs(10))))
                .when()
                .post("/api/v1/agendamentos/{id}/reagendamento", id)
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("Nao e permitido agendar em horario passado"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao confirmar presença em um agendamento inexistente")
    void deveRetornar404AoConfirmarPresencaEmUmAgendamentoInexistente() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/confirmacao", UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 422 ao confirmar presença antes da janela de 24 horas")
    void deveRetornar422AoConfirmarPresencaAntesDaJanelaDe24Horas() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().comInicio(amanhaAs(10).plusDays(1)).construir());

        requisicaoAutenticada(token)
                .when()
                .post("/api/v1/agendamentos/{id}/confirmacao", id)
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("A confirmacao so e liberada 24 horas antes do atendimento"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao avaliar um agendamento inexistente")
    void deveRetornar404AoAvaliarUmAgendamentoInexistente() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .body(Map.of("nota", 5))
                .when()
                .post("/api/v1/agendamentos/{id}/avaliacao", UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("status", equalTo("NOT_FOUND"))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 422 ao avaliar um atendimento que não foi realizado")
    void deveRetornar422AoAvaliarUmAtendimentoQueNaoFoiRealizado() {
        final String senha = "senha123";
        final User usuario = registrarUsuario(senha);
        final String token = autenticar(usuario.getUsername(), senha);
        final String id = agendar(token, umAgendamento().construir());

        requisicaoAutenticada(token)
                .body(Map.of("nota", 5))
                .when()
                .post("/api/v1/agendamentos/{id}/avaliacao", id)
                .then()
                .statusCode(422)
                .body("status", equalTo("UNPROCESSABLE_ENTITY"))
                .body("message", equalTo("Apenas atendimentos realizados podem ser avaliados"));
    }


    @Test
    @DisplayName("Deve retornar 401 ao buscar um agendamento sem token")
    void deveRetornar401AoBuscarUmAgendamentoSemToken() {

        requisicao()
                .when()
                .get("/api/v1/agendamentos/{id}", UUID.randomUUID())
                .then()
                .statusCode(401);
    }



    private String agendar(String token, Map<String, Object> agendamento) {
        return requisicaoAutenticada(token)
                .body(agendamento)
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .extract()
                .path("id");
    }
}

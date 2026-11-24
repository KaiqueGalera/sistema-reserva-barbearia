package br.ifsp.demo.api;

import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionApiTest extends ApiTestBase {

    @Test
    @DisplayName("Deve retornar identidade autenticada no endpoint hello")
    void deveRetornarIdentidadeNoEndpointHello() {
        String senha = "senha123";
        User usuario = registrarUsuario(senha);
        String token = autenticar(usuario.getUsername(), senha);

        requisicaoAutenticada(token)
                .when()
                .get("/api/v1/hello")
                .then()
                .statusCode(200)
                .body(org.hamcrest.Matchers.containsString(usuario.getId().toString()));
    }

    @Test
    @DisplayName("Deve retornar 401 no hello sem autenticação")
    void deveRetornar401NoHelloSemAutenticacao() {
        requisicao()
                .when()
                .get("/api/v1/hello")
                .then()
                .statusCode(401);
    }
}

package br.ifsp.demo.api;

import br.ifsp.demo.security.auth.AuthRequest;
import br.ifsp.demo.security.auth.RegisterUserRequest;
import br.ifsp.demo.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;

class UserApiTest extends ApiTestBase {

    @Test
    @DisplayName("Deve registrar usuário com dados válidos")
    void deveRegistrarUsuarioComDadosValidos() {
        requisicao()
                .body(new RegisterUserRequest("Ana", "Silva", "ana" + UUID.randomUUID() + "@teste.com", "senha123"))
                .when()
                .post("/api/v1/register")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 409 ao registrar email já existente")
    void deveRetornar409AoRegistrarEmailDuplicado() {
        String senha = "senha123";
        User usuario = registrarUsuario(senha);

        requisicao()
                .body(new RegisterUserRequest("Nome", "Sobrenome", usuario.getUsername(), senha))
                .when()
                .post("/api/v1/register")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("Deve retornar 401 ao autenticar com credenciais inválidas")
    void deveRetornar401AoAutenticarComCredenciaisInvalidas() {
        requisicao()
                .body(new AuthRequest("inexistente@teste.com", "senha-incorreta"))
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(401);
    }
}

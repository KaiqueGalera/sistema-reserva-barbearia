package br.ifsp.demo.api;

import br.ifsp.demo.security.auth.AuthRequest;
import br.ifsp.demo.security.auth.AuthResponse;
import br.ifsp.demo.security.user.JpaUserRepository;
import br.ifsp.demo.security.user.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;

@Tag("ApiTest")
@Tag("IntegrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/api-test.db")
public abstract class ApiTestBase {

    @LocalServerPort
    protected int port;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void configurarRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    void limparBanco() {
        jdbc.update("DELETE FROM item_de_servico");
        jdbc.update("DELETE FROM agendamento");
        userRepository.deleteAll();
    }

    protected User registrarUsuario(String senha) {
        User usuario = EntityBuilder.criarUsuarioAleatorio(passwordEncoder.encode(senha));
        return userRepository.save(usuario);
    }

    protected String autenticar(String email, String senha) {
        return requisicao()
                .body(new AuthRequest(email, senha))
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class)
                .token();
    }

    protected RequestSpecification requisicao() {
        return given().contentType(ContentType.JSON);
    }

    protected RequestSpecification requisicaoAutenticada(String token) {
        return requisicao().header("Authorization", "Bearer " + token);
    }
}

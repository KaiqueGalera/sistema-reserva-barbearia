package br.ifsp.demo.ui;

import br.ifsp.demo.controller.AgendarAtendimentoRequest;
import br.ifsp.demo.security.auth.AuthRequest;
import br.ifsp.demo.ui.UsuarioUiHelper.Credenciais;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static br.ifsp.demo.ui.ConfiguracaoUi.URL_API;
import static io.restassured.RestAssured.given;

public final class ApiUiHelper {

    private static final UUID SERVICO_CORTE_ID = UUID.fromString("11111111-1111-1111-1111-111111111111"); //corte
    private static final UUID BARBEIRO_ID = UUID.fromString("aaaaaaaa-0000-4000-8000-000000000001"); //Almir

    private ApiUiHelper() {
    }

    public static String autenticar(Credenciais c) {
        Response res = given()
                .baseUri(URL_API)
                .contentType(ContentType.JSON)
                .body(new AuthRequest(c.email(), c.senha()))
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = res.jsonPath().getString("token");
        if (token == null) {
            token = res.jsonPath().getString("accessToken");
        }
        return token;
    }

    public static Response agendarCorte(String token, int diasAFrente) {
        LocalDateTime inicio = LocalDateTime.of(
                LocalDate.now().plusDays(diasAFrente),
                LocalTime.of(10, 0)
        );

        AgendarAtendimentoRequest request = new AgendarAtendimentoRequest(
                BARBEIRO_ID,
                "Cliente Teste",
                "16999998888",
                inicio,
                List.of(SERVICO_CORTE_ID)
        );

        return given()
                .baseUri(URL_API)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/agendamentos")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }
}
package modulos.produto;

import dataactory.ProdutoDataFactory;
import dataactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@DisplayName("Testes de API REST do modulo de Produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforeEach() {

        baseURI = "http://165.227.93.41";
        basePath = "/lojinha";

        this.token = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.criarUsuarioAdministrador())
            .when()
                .post("/v2/login")
            .then()
                .extract()
                    .path("data.token");
    }


    @Test
    @DisplayName("Validar que o valor do Produto igual a 0.00 nao e permitido")
    public void testValidarLimitesZeradoProibidosValorProduto() {

       given()
               .contentType(ContentType.JSON)
               .header("token", this.token)
               .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(0.00))

       .when()
               .post("/v2/produtos")
       .then()
               .assertThat()
                    .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);

    }

    @Test
    @DisplayName("Validar que o valor do Produto igual a 7000.001nao e permitido")
    public void testValidarLimitesMaiorSeteMilProibidosValorProduto() {


        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(7000.01))

            .when()
                .post("/v2/produtos")
            .then()
                .assertThat()
                    .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);

    }

}

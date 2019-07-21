package com.golinko.statement.processor

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.parsing.Parser
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ActiveProfiles("apitest")
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [ApplicationTestConfig::class], loader = SpringBootContextLoader::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ApplicationTests {
    @LocalServerPort
    var localServerPort: Int? = null

    @Autowired
    @Qualifier("objectMapper")
    lateinit var mapper: ObjectMapper

    @Before
    fun setUp() {
        RestAssured.port = localServerPort!!
        RestAssured.baseURI = "http://localhost"
        RestAssured.basePath = "/statement-processor"
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> mapper })
        RestAssured.registerParser(ContentType.JSON.toString(), Parser.JSON)
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        RestAssured.given().contentType(ContentType.JSON)
    }

    @Test
    fun validateDemoStatements_CSV_badRequest() {
        given()
                .log().all()
                .`when`()
                .get("/demo/CSV")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("find { it.reference == 112806 }.errors.reference[0]", equalTo<String>("not unique"))
                .body("find { it.reference == 158338 }.errors.endBalance[0]", equalTo<String>("must be greater than or equal to 0"))
                .body("find { it.reference == 196213 }.errors.endBalance[0]", equalTo<String>("must be greater than or equal to 0"))
    }

    @Test
    fun validateDemoStatements_JSON_badRequest() {
        given()
                .log().all()
                .`when`()
                .get("/demo/JSON")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("find { it.reference == 140269 }.errors.endBalance[0]", equalTo<String>("not equals to sum of start balance and mutation"))
                .body("find { it.reference == 154270 }.errors.endBalance[0]", equalTo<String>("not equals to sum of start balance and mutation"))
                .body("find { it.reference == 163023 }.errors.endBalance[0]", equalTo<String>("must be greater than or equal to 0"))
    }

    @Test
    fun validateDemoStatements_XML_badRequest() {
        given()
                .log().all()
                .`when`()
                .get("/demo/XML")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("find { it.reference == 140269 }.errors.endBalance[0]", equalTo<String>("not equals to sum of start balance and mutation"))
                .body("find { it.reference == 154270 }.errors.endBalance[0]", equalTo<String>("not equals to sum of start balance and mutation"))
                .body("find { it.reference == 163023 }.errors.endBalance[0]", equalTo<String>("must be greater than or equal to 0"))
    }

    @Test
    fun validateStatements_CSV_Ok() {
        val file = ClassPathResource("records_ok.csv").file

        given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("dataFile", file)
                .post("/validate/CSV")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("isEmpty()", Matchers.`is`(true))
    }

    @Test
    fun validateStatements_XML_invalidDate_multipleEndBalance() {
        val file = ClassPathResource("records_invalid_date.xml").file

        given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("dataFile", file)
                .post("/validate/XML")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("find { it.reference == 115137 }.errors.date[0]", equalTo<String>("must be a date in the past or in the present"))
                .body("find { it.reference == 112747 }.errors.endBalance", allOf(
                        hasItem("must be greater than or equal to 0"),
                        hasItem("not equals to sum of start balance and mutation")))
    }

    @Test
    fun validateStatements_bogusMimeType() {
        val file = ClassPathResource("records_ok.csv").file

        given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("dataFile", file)
                .post("/validate/BOGUS")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun validateStatements_notMatchingMimeType() {
        val file = ClassPathResource("records_ok.csv").file

        given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("dataFile", file)
                .post("/validate/JSON")
                .then()
                .log().body()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("message", equalTo<String>("Error during json file read"))
    }
}

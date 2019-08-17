package org.ga4gh.ComplianceFramework;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestingFrameworkTest {

    private String base_url;
    private WireMockServer wireMockServer = new WireMockServer(8089);
    private RequestsRestAssured request = new RequestsRestAssured();

    @BeforeClass
    public void initializeMockServer() {
        WireMock.configureFor("localhost", 8089);
        wireMockServer.start();
        base_url = "http://localhost:8089/";

        stubFor(get(urlEqualTo("/test/success"))
                .willReturn(aResponse()
                        .withStatus(200)));

        stubFor(get(urlEqualTo("/test/success")).willReturn(aResponse().withStatus(200).withBody("test body success")));
        stubFor(get(urlEqualTo("/test/success/header")).willReturn(aResponse().withStatus(200).withHeader("Content-Type", "text").withBody("test body success")));
        stubFor(get(urlEqualTo("/test/error")).willReturn(aResponse().withStatus(500).withBody("test body error")));
        stubFor(get(urlEqualTo("/test/success/json")).willReturn(okJson("{\"test\" : \"body\"}")));
        stubFor(get(urlEqualTo("/test/success/xml")).willReturn(okXml("<test>body</test>")));
        }

    @AfterClass
    public void shutMockServer() {
        wireMockServer.stop();
    }

    @Test
    public void getBodyStringTest() {
        Response response = request.GET(base_url + "test/success");

        Assert.assertEquals(TestingFramework.getBodyString(response), "test body success");
    }

    @Test
    public void getBodyJsonTest() {
        Response response = request.GET(base_url + "test/success/json");

        Assert.assertEquals(TestingFramework.getBodyJSON(response).getString("test"), "body");
    }

    @Test
    public void getBodyXmlTest() {
        Response response = request.GET(base_url + "test/success/xml");

        Assert.assertEquals(TestingFramework.getBodyXML(response).getString("test"), "body");
    }

    @Test
    public void getStatusCodeTest() {
        Response response = request.GET(base_url + "test/success");

        Assert.assertEquals(TestingFramework.getStatusCode(response), 200);
    }

    @Test
    public void getStatusMessageTest() {
        Response response = request.GET(base_url + "test/success");

        Assert.assertEquals(TestingFramework.getStatusMessage(response), "HTTP/1.1 200 OK");
    }

    @Test
    public void checkSuccessTest() {
        Response response = request.GET(base_url + "test/success");

        Assert.assertTrue(TestingFramework.checkSuccess(response));

        response = request.GET(base_url + "test/error");

        Assert.assertFalse(TestingFramework.checkSuccess(response));
    }

    @Test
    public void validateResponseBodyWithLocalFileTest() throws IOException {
        Response response = request.GET(base_url + "test/success");

        Assert.assertTrue(TestingFramework.validateResponseBodyWithLocalFile(response, "dummy.txt"));

        response = request.GET(base_url + "test/error");

        Assert.assertFalse(TestingFramework.validateResponseBodyWithLocalFile(response, "dummy.txt"));
    }

    @Test
    public void checkHeaderPresentTest(){
        Response response = request.GET(base_url + "test/success/header");

        Assert.assertTrue(TestingFramework.checkHeaderPresent(response, "Content-Type"));
    }

    @Test
    public void validateResponseHeaderTest(){
        Response response = request.GET(base_url + "test/success/header");

        Assert.assertTrue(TestingFramework.validateResponseHeader(response, "Content-Type", "text"));
    }
}

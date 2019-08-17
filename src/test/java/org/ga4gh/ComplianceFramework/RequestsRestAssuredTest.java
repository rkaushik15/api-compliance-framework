package org.ga4gh.ComplianceFramework;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


public class RequestsRestAssuredTest {

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

        stubFor(get(urlEqualTo("/test/success")).willReturn(aResponse().withStatus(200).withBody("test body")));
        stubFor(get(urlEqualTo("/test/success")).withHeader("Accept", equalTo("text")).willReturn(aResponse().withStatus(200).withBody("test body headers")));
        stubFor(get(urlEqualTo("/test/query?param1=1&param2=2")).willReturn(aResponse().withStatus(200).withBody("test body query parameters")));
        stubFor(get(urlEqualTo("/test/query?param1=1&param2=2")).withHeader("Accept", equalTo("text")).willReturn(aResponse().withStatus(200).withBody("test body headers and query parameters")));
        stubFor(post(urlEqualTo("/test/success")).withRequestBody(equalTo("request body post")).willReturn(aResponse().withStatus(200).withBody("test body post")));
        stubFor(put(urlEqualTo("/test/success")).withRequestBody(equalTo("request body put")).willReturn(aResponse().withStatus(200).withBody("test body put")));
        stubFor(delete(urlEqualTo("/test/success")).withRequestBody(equalTo("request body delete")).willReturn(aResponse().withStatus(200).withBody("test body delete")));
    }

    @AfterClass
    public void shutMockServer() {
        wireMockServer.stop();
    }

    @Test
    public void GETTest() {
        Response response = request.GET(base_url + "test/success");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body");
    }

    @Test
    public void GETWithQueryParamsTest() {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("param1", "1");
        parameterMap.put("param2", "2");

        Response response = request.GETWithQueryParams(base_url + "test/query", parameterMap);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body query parameters");
    }

    @Test
    public void GETWithHeadersTest() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "text");

        Response response = request.GETWithHeaders(base_url + "test/success", headerMap);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body headers");
    }

    @Test
    public void GETWithHeadersAndQueryParamsTest() {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("param1", "1");
        parameterMap.put("param2", "2");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "text");

        Response response = request.GETWithHeadersAndQueryParams(base_url + "test/query", headerMap, parameterMap);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body headers and query parameters");
    }

    @Test
    public void POSTTest() {
        Response response = request.POST(base_url + "test/success", "request body post");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body post");
    }

    @Test
    public void PUTTest() {
        Response response = request.PUT(base_url + "test/success", "request body put");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body put");
    }

    @Test
    public void DELETETest() {
        Response response = request.DELETE(base_url + "test/success", "request body delete");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "test body delete");
    }
}

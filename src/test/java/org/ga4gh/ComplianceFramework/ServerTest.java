package org.ga4gh.ComplianceFramework;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ServerTest {

    private final String url = "test_url";
    private final String url2 = "test_url2";
    private final Map<String, Object> properties = new HashMap<String, Object>(){{ put("prop", "a"); }};


    @Test
    public void getServerUrlTest() {
        Server server = new Server(url);

        Assert.assertEquals(server.getBaseUrl(), url);
        Assert.assertEquals(server.getEndpoint("/test"), url + "/test");
    }

    @Test
    public void getUrlPropertiesTest() {
        Server server = new Server(url, properties);

        Assert.assertEquals(server.getBaseUrl(), url);
        Assert.assertEquals(server.getServerProperties(), properties);
    }

    @Test
    public void getServerPropertyTest() {
        Server server = new Server(url, properties);

        Assert.assertEquals(server.getBaseUrl(), url);
        Assert.assertEquals(server.getServerProperties(), properties);
        Assert.assertEquals(server.getServerProperty("prop"), "a");
    }

    @Test
    public void setAndGetServerTest() {
        Server server = new Server(url);

        server.setBaseUrl(url2);
        server.setServerProperties(properties);

        Assert.assertEquals(server.getBaseUrl(), url2);
        Assert.assertEquals(server.getServerProperties(), properties);
    }
}

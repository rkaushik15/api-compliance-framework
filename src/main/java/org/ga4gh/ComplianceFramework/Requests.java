package org.ga4gh.ComplianceFramework;

import io.restassured.response.Response;

public interface Requests {

    Response GET(String uri);

    Response POST(String uri, String body);

    Response PUT(String uri, String body);

    Response DELETE(String uri, String body);
}

package org.ga4gh.ComplianceFramework;

import io.restassured.response.Response;

import java.util.Map;

public interface Requests {

    Response GET(String uri);

    Response GETWithQueryParams(String uri, Map<String, String> parameterMap);

    Response GETWithHeaders(String uri, Map<String, String> headerMap);

    Response GETWithHeadersAndQueryParams(String uri, Map<String, String> headerMap, Map<String, String> parameterMap);

    Response POST(String uri, String body);

    Response PUT(String uri, String body);

    Response DELETE(String uri, String body);
}

package com.github.rest.yaml.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Map;

import com.github.rest.yaml.test.beans.YamlTest;
import com.github.rest.yaml.test.util.Logger;
import com.jayway.restassured.response.Response;

public class RestResponse {
	private static Logger logger = new Logger();
	private Response response;
	private YamlTest yamlTest;

	public static RestResponse build(Response response, YamlTest yamlTest) {
		return new RestResponse(response, yamlTest);
	}

	private RestResponse(Response response, YamlTest yamlTest) {
		this.response = response;
		this.yamlTest = yamlTest;
	}

	public void doAssert() {
		assertThat(response.statusCode(), equalTo(yamlTest.getResponse().getStatus()));
		CurrentState.getYamlInitGroup().storeVariableValue(yamlTest, response);
		headersAssert();
		cookiesAssert();
		BodyAssert.build(response, yamlTest).doAssert();
	}

	private void headersAssert() {
		logger.debug("response headers = " + response.getHeaders());
		if (yamlTest.getResponse().getHeaders() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getHeaders().entrySet()) {
				String actual = response.getHeader(entry.getKey());
				String expected = entry.getValue();
				logger.info("Header assert=" + entry.getKey() + ", expected=" + expected + " actual=" + actual);
				assertThat(actual, equalTo(expected));
			}
		}
	}

	private void cookiesAssert() {
		logger.debug("response cookies = " + response.getCookies());
		if (yamlTest.getResponse().getCookies() != null) {
			for (Map.Entry<String, String> entry : yamlTest.getResponse().getCookies().entrySet()) {
				String actual = response.getCookie(entry.getKey());
				String expected = entry.getValue();
				logger.info("Cookie assert=" + entry.getKey() + ", expected=" + expected + " actual=" + actual);
				assertThat(actual, equalTo(expected));
			}
		}
	}
}

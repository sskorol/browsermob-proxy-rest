package com.blogspot.notes.automation.qa.proxy;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

public class BrowsermobProxyRestClient {

	private Client client;
	private WebTarget service;

	private ObjectMapper mapper = new ObjectMapper();

	private int port;

	private static final int PORT_BEGIN_INDEX = 8;
	private static final int PORT_END_INDEX = 12;

	private static final int HTTP_STATUS_CODE_200 = 200;
	private static final int HTTP_STATUS_CODE_204 = 204;

	public BrowsermobProxyRestClient(final String host, final int port) {
		this.client = ClientBuilder.newClient();
		this.service = client.target("http://" + host + ":" + port + "/proxy");
	}

	public void dispose() {
		client.close();
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public int getPortUsingUpstreamProxy(final String upStreamProxyServer) {
		final Form formData = new Form()
				.param("httpProxy", upStreamProxyServer);

		return Integer.parseInt(service.request()
				.post(Entity.form(formData))
				.readEntity(String.class)
				.substring(PORT_BEGIN_INDEX, PORT_END_INDEX));
	}

	public int getPortAndTrustAllServers() {
		final Form formData = new Form()
				.param("trustAllServers", "true");

		return Integer.parseInt(service.request()
				.post(Entity.form(formData))
				.readEntity(String.class)
				.substring(PORT_BEGIN_INDEX, PORT_END_INDEX));
	}

	public boolean addHeader(final Map<String, String> headers) throws IOException {
		return service.path(Integer.toString(port))
				.path("headers")
				.request()
				.post(Entity.text(mapper.writeValueAsString(headers)))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean blacklistRequests(final String pattern, final int responseCode) {
		final Form formData = new Form()
				.param("regex", pattern)
				.param("status", Integer.toString(responseCode));

		return service.path(Integer.toString(port))
				.path("blacklist")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean clearDNSCache() {
		return service.path(Integer.toString(port))
				.path("dns")
				.path("cache")
				.request()
				.delete(Response.class)
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public String getHarAsString() {
		return service.path(Integer.toString(port))
				.path("har")
				.request()
				.get(Response.class)
				.readEntity(String.class);
	}

	public Integer getPort() {
		return Integer.parseInt(service.request()
				.post(null)
				.readEntity(String.class)
				.substring(PORT_BEGIN_INDEX, PORT_END_INDEX));
	}

	public String getPortStr() {
		return service.request()
				.post(null)
				.readEntity(String.class);
	}

	public int getPort(final int port) {
		final Form formData = new Form()
				.param("port", Integer.toString(port));

		return Integer.parseInt(service.request().post(Entity.form(formData))
				.readEntity(String.class)
				.substring(PORT_BEGIN_INDEX, PORT_END_INDEX));
	}

	public boolean enableLimiter(final boolean status) {
		final Form formData = new Form()
				.param("enable", Boolean.toString(status));

		return service.path(Integer.toString(port))
				.path("limit")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setDownstreamKbps(final long downstreamKbps) {
		final Form formData = new Form()
				.param("downstreamKbps", Long.toString(downstreamKbps));

		return service.path(Integer.toString(port))
				.path("limit")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setLatency(final long latency) {
		final Form formData = new Form()
				.param("latency", Long.toString(latency));

		return service.path(Integer.toString(port))
				.path("limit")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setMaxBitsPerSecondThreshold(final long maxBitsPerSecond) {
		final Form formData = new Form()
				.param("maxBitsPerSecond", Long.toString(maxBitsPerSecond));

		return service.path(Integer.toString(port))
				.path("limit")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setPayloadPercentage(final long payloadPercentage) {
		final Form formData = new Form()
				.param("payloadPercentage", Long.toString(payloadPercentage));

		return service.path(Integer.toString(port))
				.path("limit")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setUpstreamKbps(final long upstreamKbps) {
		final Form formData = new Form()
				.param("upstreamKbps", Long.toString(upstreamKbps));

		return service.path(Integer.toString(port))
				.path("limit")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean newHar(final String initialPageRef, final boolean captureContent,
						  final boolean captureHeaders, final boolean captureBinaryContent) {
		final Form formData = new Form();

		if (!initialPageRef.isEmpty()) {
			formData.param("initialPageRef", initialPageRef);
		}

		if (captureContent) {
			formData.param("captureContent", Boolean.toString(captureContent));
		}

		if (captureHeaders) {
			formData.param("captureHeaders", Boolean.toString(captureHeaders));
		}

		if (captureBinaryContent) {
			formData.param("captureBinaryContent",
					Boolean.toString(captureBinaryContent));
		}

		return service.path(Integer.toString(port))
				.path("har")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_204;
	}

	public boolean newPage(final String pageRef) {
		final Form formData = new Form();

		if (!pageRef.isEmpty()) {
			formData.param("pageRef", pageRef);
		}

		return service.path(Integer.toString(port))
				.path("har")
				.path("pageRef")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean remapHost(Map<String, String> hosts) throws IOException {
		return service.path(Integer.toString(port))
				.path("hosts")
				.request()
				.post(Entity.text(mapper.writeValueAsString(hosts)))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean rewriteUrl(final String matchRegex, final String replace) {
		final Form formData = new Form()
				.param("matchRegex", matchRegex)
				.param("replace", replace);

		return service.path(Integer.toString(port))
				.path("rewrite")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setConnectionTimeout(final long connectionTimeout) {
		final Form formData = new Form()
				.param("connectionTimeout", Long.toString(connectionTimeout));

		return service.path(Integer.toString(port))
				.path("timeout")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setDNSCacheTimeout(final long dnsCacheTimeout) {
		final Form formData = new Form()
				.param("dnsCacheTimeout", Long.toString(dnsCacheTimeout));

		return service.path(Integer.toString(port))
				.path("timeout")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setRequestTimeout(final long requestTimeout) {
		final Form formData = new Form()
				.param("requestTimeout", Long.toString(requestTimeout));

		return service.path(Integer.toString(port))
				.path("timeout")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setRetryCount(final int retrycount) {
		final Form formData = new Form()
				.param("retrycount", Integer.toString(retrycount));

		return service.path(Integer.toString(port))
				.path("retry")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean setSocketOperationTimeout(final long readTimeout) {
		final Form formData = new Form()
				.param("readTimeout", Long.toString(readTimeout));

		return service.path(Integer.toString(port))
				.path("timeout")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean stop() {
		return service.path(Integer.toString(port))
				.request()
				.delete(Response.class)
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean waitForNetworkTrafficToStop(final long quietPeriodInMs,
											   final long timeoutInMs) {
		final Form formData = new Form()
				.param("quietPeriodInMs", Long.toString(quietPeriodInMs))
				.param("timeoutInMs", Long.toString(timeoutInMs));

		return service.path(Integer.toString(port))
				.path("wait")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}

	public boolean whitelistRequests(final String patterns, final int responseCode) {
		final Form formData = new Form()
				.param("regex", patterns)
				.param("status", Integer.toString(responseCode));

		return service.path(Integer.toString(port))
				.path("whitelist")
				.request()
				.put(Entity.form(formData))
				.getStatus() == HTTP_STATUS_CODE_200;
	}
}


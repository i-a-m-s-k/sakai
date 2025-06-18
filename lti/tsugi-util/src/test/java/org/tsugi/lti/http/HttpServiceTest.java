package org.tsugi.lti.http;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HttpServiceTest {
    private HttpService httpService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        httpService = new HttpService();
    }

    @Test
    public void testCreateMessageFromRequest() throws IOException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.com/lti"));
        
        Vector<String> headerNames = new Vector<>();
        headerNames.add("Content-Type");
        headerNames.add("Authorization");
        Enumeration<String> headerEnum = headerNames.elements();
        when(request.getHeaderNames()).thenReturn(headerEnum);
        when(request.getHeader("Content-Type")).thenReturn("application/x-www-form-urlencoded");
        when(request.getHeader("Authorization")).thenReturn("OAuth realm=\"example.com\"");
        
        Vector<String> paramNames = new Vector<>();
        paramNames.add("lti_message_type");
        paramNames.add("lti_version");
        Enumeration<String> paramEnum = paramNames.elements();
        when(request.getParameterNames()).thenReturn(paramEnum);
        when(request.getParameter("lti_message_type")).thenReturn("basic-lti-launch-request");
        when(request.getParameter("lti_version")).thenReturn("LTI-1p0");

        HttpMessage message = httpService.createMessageFromRequest(request);

        assertEquals("POST", message.getMethod());
        assertEquals("http://example.com/lti", message.getUrl());
        assertEquals("application/x-www-form-urlencoded", message.getHeader("Content-Type"));
        assertEquals("OAuth realm=\"example.com\"", message.getHeader("Authorization"));
        assertEquals("basic-lti-launch-request", message.getParameter("lti_message_type"));
        assertEquals("LTI-1p0", message.getParameter("lti_version"));
    }

    @Test
    public void testPostLaunchHTML() {
        Map<String, String> properties = new HashMap<>();
        properties.put("lti_message_type", "basic-lti-launch-request");
        properties.put("lti_version", "LTI-1p0");
        String endpoint = "http://example.com/lti";
        String launchText = "Launch Tool";

        String html = httpService.postLaunchHTML(properties, endpoint, launchText, true, false);

        assertTrue(html.contains("<form"));
        assertTrue(html.contains("action=\"" + endpoint + "\""));
        assertTrue(html.contains("name=\"lti_message_type\""));
        assertTrue(html.contains("name=\"lti_version\""));
        assertTrue(html.contains("value=\"" + launchText + "\""));
        assertTrue(html.contains("doSubmit()"));
    }

    @Test
    public void testSendHtmlResponse() throws IOException {

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        String html = "<html><body>Test</body></html>";
        httpService.sendHtmlResponse(response, html);

        verify(response).setContentType("text/html; charset=UTF-8");
        assertEquals(html, writer.toString());
    }

    @Test
    public void testJsonSerialization() throws IOException {
        HttpMessage message = new HttpMessage("POST", "http://example.com/lti");
        message.addHeader("Content-Type", "application/json");
        message.addParameter("lti_message_type", "basic-lti-launch-request");
        String json = httpService.toJson(message);
        HttpMessage deserialized = httpService.fromJson(json, HttpMessage.class);
        assertEquals(message.getMethod(), deserialized.getMethod());
        assertEquals(message.getUrl(), deserialized.getUrl());
        assertEquals(message.getHeader("Content-Type"), deserialized.getHeader("Content-Type"));
        assertEquals(message.getParameter("lti_message_type"), deserialized.getParameter("lti_message_type"));
    }
} 
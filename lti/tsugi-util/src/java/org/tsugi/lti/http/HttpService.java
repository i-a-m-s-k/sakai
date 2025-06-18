package org.tsugi.lti.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.tsugi.lti.oauth.OAuthMessage;
import org.tsugi.lti.oauth.OAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpService {
    private final ObjectMapper objectMapper;
    private final OAuthService oauthService;

    public HttpService() {
        this.objectMapper = new ObjectMapper();
        this.oauthService = new OAuthService();
    }

    public HttpMessage createMessageFromRequest(HttpServletRequest request) throws IOException {
        HttpMessage message = new HttpMessage(request.getMethod(), request.getRequestURL().toString());
        
        // Add headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            message.addHeader(headerName, request.getHeader(headerName));
        }

        // Add parameters
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            message.addParameter(paramName, request.getParameter(paramName));
        }

        return message;
    }

    public String postLaunchHTML(Map<String, String> properties, String endpoint, 
                               String launchText, boolean autoSubmit, boolean debug) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("<title>LTI Launch</title>\n");
        html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
        
        if (autoSubmit) {
            html.append("<script type=\"text/javascript\">\n");
            html.append("function doSubmit() {\n");
            html.append("  document.getElementById('ltiLaunchForm').submit();\n");
            html.append("}\n");
            html.append("</script>\n");
        }
        
        html.append("</head>\n");
        html.append("<body>\n");
        
        if (debug) {
            html.append("<h1>LTI Debug Information</h1>\n");
            html.append("<pre>\n");
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                html.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
            html.append("</pre>\n");
        }
        
        html.append("<form id=\"ltiLaunchForm\" action=\"").append(endpoint).append("\" method=\"post\">\n");
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            html.append("<input type=\"hidden\" name=\"").append(entry.getKey())
                .append("\" value=\"").append(entry.getValue()).append("\"/>\n");
        }
        html.append("<input type=\"submit\" value=\"").append(launchText).append("\"/>\n");
        html.append("</form>\n");
        
        if (autoSubmit) {
            html.append("<script type=\"text/javascript\">\n");
            html.append("doSubmit();\n");
            html.append("</script>\n");
        }
        
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }

    public HttpURLConnection sendOAuthRequest(String method, String url, 
                                            String oauthKey, String oauthSecret) throws IOException {
        OAuthMessage oauthMessage = oauthService.signProperties(new HashMap<>(), url, method, oauthKey, oauthSecret);
        return sendRequest(method, url, oauthMessage.getParameters());
    }

    public HttpURLConnection sendRequest(String method, String url, Map<String, String> parameters) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        
        // Add parameters
        if (parameters != null && !parameters.isEmpty()) {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (postData.length() > 0) {
                    postData.append('&');
                }
                postData.append(entry.getKey()).append('=').append(entry.getValue());
            }
            
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(postData.toString());
                writer.flush();
            }
        }
        
        return connection;
    }

    public String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    public void sendHtmlResponse(HttpServletResponse response, String html) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }

    public String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T fromJson(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }
} 
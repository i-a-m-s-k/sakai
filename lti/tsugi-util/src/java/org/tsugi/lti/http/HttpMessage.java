package org.tsugi.lti.http;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JacksonXmlRootElement(localName = "http_message")
public class HttpMessage {
    @JacksonXmlProperty
    private String method;

    @JacksonXmlProperty
    private String url;

    @JacksonXmlProperty
    private Map<String, String> headers;

    @JacksonXmlProperty
    private Map<String, String> parameters;

    public HttpMessage() {
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
    }

    public HttpMessage(String method, String url) {
        this();
        this.method = method;
        this.url = url;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    @JsonIgnore
    public String getHeader(String name) {
        return headers.get(name);
    }

    @JsonIgnore
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @JsonIgnore
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    @JsonIgnore
    public boolean hasParameter(String name) {
        return parameters.containsKey(name);
    }
} 
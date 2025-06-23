package org.tsugi.lti.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.Data;
import lombok.ToString;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

@JacksonXmlRootElement(localName = "basic_lti_link")
@Data
@ToString
public class LTIDescriptor {
    @JacksonXmlProperty(localName = "title", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String title;

    @JacksonXmlProperty(localName = "description", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String description;

    @JacksonXmlProperty(localName = "custom", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private Custom custom;

    @JacksonXmlProperty(localName = "extensions", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private List<Extension> extensions;

    @JacksonXmlProperty(localName = "launch_url", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String launchUrl;

    @JacksonXmlProperty(localName = "secure_launch_url", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String secureLaunchUrl;

    @JacksonXmlProperty(localName = "icon", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String icon;

    @JacksonXmlProperty(localName = "secure_icon", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String secureIcon;

    @JacksonXmlProperty(localName = "cartridge_icon", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private CartridgeIcon cartridgeIcon;

    @JacksonXmlProperty(localName = "vendor", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private Vendor vendor;

    @Data
    @ToString
    public static class Custom {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "parameter", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private List<Parameter> parameters;
    }

    @Data
    @ToString
    public static class Extension {
        @JacksonXmlProperty(isAttribute = true, localName = "platform")
        private String platform;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "parameter", namespace = "")
        private List<Parameter> parameters;
    }

    @Data
    @ToString
    @JacksonXmlRootElement(localName = "parameter")
    public static class Parameter {
        @JacksonXmlProperty(isAttribute = true, localName = "key")
        private String key;

        @JacksonXmlText
        @JacksonXmlCData
        private String value;
    }

    @Data
    @ToString
    public static class CartridgeIcon {
        @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
        private String identifierref;
    }

    @Data
    @ToString
    public static class Vendor {
        @JacksonXmlProperty(localName = "code", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private String code;

        @JacksonXmlProperty(localName = "name", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private String name;

        @JacksonXmlProperty(localName = "description", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private String description;

        @JacksonXmlProperty(localName = "contact", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private Contact contact;

        @JacksonXmlProperty(localName = "url", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private String url;
    }

    @Data
    @ToString
    public static class Contact {
        @JacksonXmlProperty(localName = "email", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
        private String email;
    }
} 
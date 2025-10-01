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
    
} 
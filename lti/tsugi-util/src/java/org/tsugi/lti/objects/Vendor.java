package org.tsugi.lti.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Vendor {
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



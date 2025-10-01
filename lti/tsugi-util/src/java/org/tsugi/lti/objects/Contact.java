package org.tsugi.lti.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Contact {
    @JacksonXmlProperty(localName = "email", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private String email;
}



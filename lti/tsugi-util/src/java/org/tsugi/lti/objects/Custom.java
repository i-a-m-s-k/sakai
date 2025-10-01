package org.tsugi.lti.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@ToString
public class Custom {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "parameter", namespace = "http://www.imsglobal.org/xsd/imsbasiclti_v1p0")
    private List<Parameter> parameters;
}



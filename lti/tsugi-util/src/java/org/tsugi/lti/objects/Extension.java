package org.tsugi.lti.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@ToString
public class Extension {
    @JacksonXmlProperty(isAttribute = true, localName = "platform")
    private String platform;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "parameter", namespace = "")
    private List<Parameter> parameters;
}



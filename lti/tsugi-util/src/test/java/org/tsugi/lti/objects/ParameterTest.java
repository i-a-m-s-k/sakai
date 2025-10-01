package org.tsugi.lti.objects;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParameterTest {

    @Test
    public void testParameterSettersAndGetters() {
        Parameter parameter = new Parameter();
        parameter.setKey("keyname");
        parameter.setValue("value");

        assertEquals("keyname", parameter.getKey());
        assertEquals("value", parameter.getValue());
    }
}



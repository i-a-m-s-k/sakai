package org.tsugi.lti.objects;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class CompositeObjectsTest {

    @Test
    public void testCustomWithParameters() {
        Parameter p1 = new Parameter();
        p1.setKey("key1");
        p1.setValue("value1");

        Parameter p2 = new Parameter();
        p2.setKey("key2");
        p2.setValue("value2");

        Custom custom = new Custom();
        custom.setParameters(Arrays.asList(p1, p2));

        assertEquals(2, custom.getParameters().size());
        assertEquals("key1", custom.getParameters().get(0).getKey());
        assertEquals("value2", custom.getParameters().get(1).getValue());
    }

    @Test
    public void testExtensionWithPlatformAndParameters() {
        Parameter p = new Parameter();
        p.setKey("extkey");
        p.setValue("extval");

        Extension extension = new Extension();
        extension.setPlatform("www.lms.com");
        extension.setParameters(Arrays.asList(p));

        assertEquals("www.lms.com", extension.getPlatform());
        assertEquals(1, extension.getParameters().size());
        assertEquals("extkey", extension.getParameters().get(0).getKey());
    }

    @Test
    public void testCartridgeIcon() {
        CartridgeIcon icon = new CartridgeIcon();
        icon.setIdentifierref("LTI001_Icon");
        assertEquals("LTI001_Icon", icon.getIdentifierref());
    }
}



package org.tsugi.lti.objects;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContactTest {

    @Test
    public void testContactSettersAndGetters() {
        Contact contact = new Contact();
        contact.setEmail("user@example.com");
        assertEquals("user@example.com", contact.getEmail());
    }
}



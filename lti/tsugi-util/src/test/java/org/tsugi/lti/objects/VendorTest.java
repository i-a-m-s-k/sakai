package org.tsugi.lti.objects;

import org.junit.Test;
import static org.junit.Assert.*;

public class VendorTest {

    @Test
    public void testVendorSettersAndGetters() {
        Vendor vendor = new Vendor();
        Contact contact = new Contact();
        contact.setEmail("support@vendor.com");

        vendor.setCode("vendor.com");
        vendor.setName("Vendor Name");
        vendor.setDescription("Vendor Description");
        vendor.setContact(contact);
        vendor.setUrl("http://vendor.com");

        assertEquals("vendor.com", vendor.getCode());
        assertEquals("Vendor Name", vendor.getName());
        assertEquals("Vendor Description", vendor.getDescription());
        assertEquals("support@vendor.com", vendor.getContact().getEmail());
        assertEquals("http://vendor.com", vendor.getUrl());
    }
}



package org.tsugi.lti;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.tsugi.lti.objects.LTIDescriptor;

public class LTIUtilJacksonTest {

    private static final String SAMPLE_DESCRIPTOR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
            "  <title>Test Tool</title>" +
            "  <description>Test Description</description>" +
            "  <custom>" +
            "    <parameter key=\"keyname\">value</parameter>" +
            "    <parameter key=\"another_key\">another_value</parameter>" +
            "  </custom>" +
            "  <extensions platform=\"www.lms.com\">" +
            "    <parameter key=\"extkey\">extvalue</parameter>" +
            "  </extensions>" +
            "  <launch_url>http://example.com/launch</launch_url>" +
            "  <secure_launch_url>https://example.com/launch</secure_launch_url>" +
            "  <icon>http://example.com/icon.png</icon>" +
            "  <secure_icon>https://example.com/icon.png</secure_icon>" +
            "  <cartridge_icon identifierref=\"LTI001_Icon\"/>" +
            "  <vendor>" +
            "    <code>vendor.com</code>" +
            "    <name>Vendor Name</name>" +
            "    <description>This is a test vendor description.</description>" +
            "    <contact>" +
            "      <email>support@vendor.com</email>" +
            "    </contact>" +
            "    <url>http://www.vendor.com/product</url>" +
            "  </vendor>" +
            "</basic_lti_link>";

    private static final String SIMPLE_DESCRIPTOR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
            "  <title>Simple Tool</title>" +
            "  <launch_url>http://simple.com/launch</launch_url>" +
            "</basic_lti_link>";

    private static final String SECURE_ONLY_DESCRIPTOR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
            "  <title>Secure Tool</title>" +
            "  <secure_launch_url>https://secure.com/launch</secure_launch_url>" +
            "</basic_lti_link>";

    private Map<String, String> launchInfo;
    private Map<String, String> postProp;

    @Before
    public void setUp() {
        launchInfo = new HashMap<>();
        postProp = new HashMap<>();
    }

    // Unit Tests for Jackson Methods

    @Test
    public void testParseDescriptorJackson_Success() {
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, SAMPLE_DESCRIPTOR);
        
        assertTrue("parseDescriptorJackson should return true for valid descriptor", result);
        
        assertEquals("http://example.com/launch", launchInfo.get("launch_url"));
        assertEquals("https://example.com/launch", launchInfo.get("secure_launch_url"));
        
        assertEquals("Test Tool", launchInfo.get("title"));
        assertEquals("Test Description", launchInfo.get("description"));
        assertEquals("http://example.com/icon.png", launchInfo.get("icon"));
        assertEquals("https://example.com/icon.png", launchInfo.get("secure_icon"));
        
        assertEquals("value", postProp.get("custom_keyname"));
        assertEquals("another_value", postProp.get("custom_another_key"));
        
        assertEquals("extvalue", postProp.get("custom_extkey"));
        
        assertEquals("vendor.com", launchInfo.get("vendor_code"));
        assertEquals("Vendor Name", launchInfo.get("vendor_name"));
        assertEquals("This is a test vendor description.", launchInfo.get("vendor_description"));
        assertEquals("http://www.vendor.com/product", launchInfo.get("vendor_url"));
        assertEquals("support@vendor.com", launchInfo.get("vendor_contact_email"));
    }

    @Test
    public void testParseDescriptorJackson_SimpleDescriptor() {
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, SIMPLE_DESCRIPTOR);
        
        assertTrue("parseDescriptorJackson should return true for simple descriptor", result);
        assertEquals("http://simple.com/launch", launchInfo.get("launch_url"));
        assertEquals("Simple Tool", launchInfo.get("title"));
        assertNull("secure_launch_url should be null", launchInfo.get("secure_launch_url"));
    }

    @Test
    public void testParseDescriptorJackson_SecureOnlyDescriptor() {
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, SECURE_ONLY_DESCRIPTOR);
        
        assertTrue("parseDescriptorJackson should return true for secure-only descriptor", result);
        assertEquals("https://secure.com/launch", launchInfo.get("secure_launch_url"));
        assertEquals("Secure Tool", launchInfo.get("title"));
        assertNull("launch_url should be null", launchInfo.get("launch_url"));
    }

    @Test
    public void testParseDescriptorJackson_NullDescriptor() {
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, null);
        assertFalse("parseDescriptorJackson should return false for null descriptor", result);
    }

    @Test
    public void testParseDescriptorJackson_EmptyDescriptor() {
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, "");
        assertFalse("parseDescriptorJackson should return false for empty descriptor", result);
    }

    @Test
    public void testParseDescriptorJackson_InvalidXML() {
        String invalidXml = "<invalid>xml</invalid>";
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, invalidXml);
        assertFalse("parseDescriptorJackson should return false for invalid XML", result);
    }

    @Test
    public void testValidateDescriptorJackson_Success() {
        String result = LTIUtil.validateDescriptorJackson(SAMPLE_DESCRIPTOR);
        assertEquals("Should return secure launch URL when available", 
                    "https://example.com/launch", result);
    }

    @Test
    public void testValidateDescriptorJackson_SimpleDescriptor() {
        String result = LTIUtil.validateDescriptorJackson(SIMPLE_DESCRIPTOR);
        assertEquals("Should return launch URL when no secure URL available", 
                    "http://simple.com/launch", result);
    }

    @Test
    public void testValidateDescriptorJackson_SecureOnlyDescriptor() {
        String result = LTIUtil.validateDescriptorJackson(SECURE_ONLY_DESCRIPTOR);
        assertEquals("Should return secure launch URL", 
                    "https://secure.com/launch", result);
    }

    @Test
    public void testValidateDescriptorJackson_NullDescriptor() {
        String result = LTIUtil.validateDescriptorJackson(null);
        assertNull("Should return null for null descriptor", result);
    }

    @Test
    public void testValidateDescriptorJackson_NoLaunchURLs() {
        String noLaunchDescriptor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title>No Launch URLs</title>" +
                "</basic_lti_link>";
        
        String result = LTIUtil.validateDescriptorJackson(noLaunchDescriptor);
        assertNull("Should return null when no launch URLs are present", result);
    }

    @Test
    public void testPrepareForExportJackson_Success() {
        String result = LTIUtil.prepareForExportJackson(SAMPLE_DESCRIPTOR);
        
        assertNotNull("prepareForExportJackson should return non-null result", result);
        assertTrue("Result should contain basic_lti_link", result.contains("basic_lti_link"));
        assertTrue("Result should contain title", result.contains("Test Tool"));
        assertTrue("Result should contain launch_url", result.contains("http://example.com/launch"));
        assertTrue("Result should contain secure_launch_url", result.contains("https://example.com/launch"));
    }

    @Test
    public void testPrepareForExportJackson_NullDescriptor() {
        String result = LTIUtil.prepareForExportJackson(null);
        assertNull("Should return null for null descriptor", result);
    }

    // Comparison Tests: Jackson vs XMLMap

    @Test
    public void testComparison_ParseDescriptor() {
        Map<String, String> jacksonLaunchInfo = new HashMap<>();
        Map<String, String> jacksonPostProp = new HashMap<>();
        boolean jacksonResult = LTIUtil.parseDescriptorJackson(jacksonLaunchInfo, jacksonPostProp, SAMPLE_DESCRIPTOR);
        
        Map<String, String> xmlMapLaunchInfo = new HashMap<>();
        Map<String, String> xmlMapPostProp = new HashMap<>();
        boolean xmlMapResult = LTIUtil.parseDescriptor(xmlMapLaunchInfo, xmlMapPostProp, SAMPLE_DESCRIPTOR);
        
        assertTrue("Jackson method should succeed", jacksonResult);
        assertTrue("XMLMap method should succeed", xmlMapResult);
        
        assertEquals("Launch URLs should match", 
                    xmlMapLaunchInfo.get("launch_url"), 
                    jacksonLaunchInfo.get("launch_url"));
        assertEquals("Secure launch URLs should match", 
                    xmlMapLaunchInfo.get("secure_launch_url"), 
                    jacksonLaunchInfo.get("secure_launch_url"));
        
        for (String key : xmlMapPostProp.keySet()) {
            if (key.startsWith("custom_")) {
                assertEquals("Custom parameter " + key + " should match", 
                            xmlMapPostProp.get(key), 
                            jacksonPostProp.get(key));
            }
        }
    }

    @Test
    public void testComparison_ValidateDescriptor() {
        String jacksonResult = LTIUtil.validateDescriptorJackson(SAMPLE_DESCRIPTOR);
        
        String xmlMapResult = LTIUtil.validateDescriptor(SAMPLE_DESCRIPTOR);
        
        assertEquals("Validation results should match", xmlMapResult, jacksonResult);
    }

    @Test
    public void testComparison_ValidateDescriptor_Simple() {
        String jacksonResult = LTIUtil.validateDescriptorJackson(SIMPLE_DESCRIPTOR);
        
        String xmlMapResult = LTIUtil.validateDescriptor(SIMPLE_DESCRIPTOR);
        
        assertEquals("Validation results should match for simple descriptor", xmlMapResult, jacksonResult);
    }

    @Test
    public void testComparison_ValidateDescriptor_SecureOnly() {
        String jacksonResult = LTIUtil.validateDescriptorJackson(SECURE_ONLY_DESCRIPTOR);
        
        String xmlMapResult = LTIUtil.validateDescriptor(SECURE_ONLY_DESCRIPTOR);
        
        assertEquals("Validation results should match for secure-only descriptor", xmlMapResult, jacksonResult);
    }

    // Edge Case Tests

    @Test
    public void testParseDescriptorJackson_EmptyCustomParameters() {
        String descriptorWithEmptyCustom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title>Empty Custom</title>" +
                "  <launch_url>http://example.com/launch</launch_url>" +
                "  <custom></custom>" +
                "</basic_lti_link>";
        
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, descriptorWithEmptyCustom);
        assertTrue("Should handle empty custom section", result);
        assertEquals("http://example.com/launch", launchInfo.get("launch_url"));
    }

    @Test
    public void testParseDescriptorJackson_MissingValues() {
        String descriptorWithMissingValues = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title></title>" +
                "  <launch_url>http://example.com/launch</launch_url>" +
                "  <custom>" +
                "    <parameter key=\"key1\"></parameter>" +
                "    <parameter key=\"\">value2</parameter>" +
                "  </custom>" +
                "</basic_lti_link>";
        
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, descriptorWithMissingValues);
        assertTrue("Should handle missing values gracefully", result);
        assertEquals("http://example.com/launch", launchInfo.get("launch_url"));
        
        assertFalse("Should not add parameter with empty key", postProp.containsKey("custom_"));
        assertFalse("Should not add parameter with empty value", postProp.containsKey("custom_key1"));
    }

    @Test
    public void testParseDescriptorJackson_SpecialCharacters() {
        String descriptorWithSpecialChars = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title>Special &amp; Characters</title>" +
                "  <launch_url>http://example.com/launch</launch_url>" +
                "  <custom>" +
                "    <parameter key=\"key:with:colons\">value with spaces</parameter>" +
                "  </custom>" +
                "</basic_lti_link>";
        
        boolean result = LTIUtil.parseDescriptorJackson(launchInfo, postProp, descriptorWithSpecialChars);
        assertTrue("Should handle special characters", result);
        assertEquals("Special & Characters", launchInfo.get("title"));
        assertEquals("value with spaces", postProp.get("custom_key_with_colons"));
    }

    // Performance Comparison Test

    @Test
    public void testPerformanceComparison() {
        for (int i = 0; i < 10; i++) {
            LTIUtil.parseDescriptorJackson(launchInfo, postProp, SAMPLE_DESCRIPTOR);
            LTIUtil.parseDescriptor(launchInfo, postProp, SAMPLE_DESCRIPTOR);
        }
        
        long jacksonStart = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            Map<String, String> testLaunchInfo = new HashMap<>();
            Map<String, String> testPostProp = new HashMap<>();
            LTIUtil.parseDescriptorJackson(testLaunchInfo, testPostProp, SAMPLE_DESCRIPTOR);
        }
        long jacksonTime = System.nanoTime() - jacksonStart;
        
        long xmlMapStart = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            Map<String, String> testLaunchInfo = new HashMap<>();
            Map<String, String> testPostProp = new HashMap<>();
            LTIUtil.parseDescriptor(testLaunchInfo, testPostProp, SAMPLE_DESCRIPTOR);
        }
        long xmlMapTime = System.nanoTime() - xmlMapStart;
        
        System.out.println("Jackson parsing time: " + jacksonTime + " ns");
        System.out.println("XMLMap parsing time: " + xmlMapTime + " ns");
        
        assertTrue("Jackson method should complete", jacksonTime > 0);
        assertTrue("XMLMap method should complete", xmlMapTime > 0);
    }
} 
package org.tsugi.lti;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.tsugi.lti.objects.LTIDescriptor;

/**
 * Test class for refactored Jackson-based LTI utility classes.
 * 
 * This class tests the separated LTIJacksonParser, LTIJacksonValidator, and LTIJacksonExporter classes.
 */
public class LTIJacksonRefactoredTest {

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

    // Unit Tests for LTIJacksonParser

    @Test
    public void testLTIJacksonParser_ParseDescriptor_Success() {
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, SAMPLE_DESCRIPTOR);
        
        assertTrue("parseDescriptor should return true for valid descriptor", result);
        
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
    public void testLTIJacksonParser_ParseDescriptor_SimpleDescriptor() {
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, SIMPLE_DESCRIPTOR);
        
        assertTrue("parseDescriptor should return true for simple descriptor", result);
        assertEquals("http://simple.com/launch", launchInfo.get("launch_url"));
        assertEquals("Simple Tool", launchInfo.get("title"));
        assertNull("secure_launch_url should be null", launchInfo.get("secure_launch_url"));
    }

    @Test
    public void testLTIJacksonParser_ParseDescriptor_SecureOnlyDescriptor() {
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, SECURE_ONLY_DESCRIPTOR);
        
        assertTrue("parseDescriptor should return true for secure-only descriptor", result);
        assertEquals("https://secure.com/launch", launchInfo.get("secure_launch_url"));
        assertEquals("Secure Tool", launchInfo.get("title"));
        assertNull("launch_url should be null", launchInfo.get("launch_url"));
    }

    @Test
    public void testLTIJacksonParser_ParseDescriptor_NullDescriptor() {
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, null);
        assertFalse("parseDescriptor should return false for null descriptor", result);
    }

    @Test
    public void testLTIJacksonParser_ParseDescriptor_EmptyDescriptor() {
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, "");
        assertFalse("parseDescriptor should return false for empty descriptor", result);
    }

    @Test
    public void testLTIJacksonParser_ParseDescriptor_InvalidXML() {
        String invalidXml = "<invalid>xml</invalid>";
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, invalidXml);
        assertFalse("parseDescriptor should return false for invalid XML", result);
    }

    // Unit Tests for LTIJacksonValidator

    @Test
    public void testLTIJacksonValidator_ValidateDescriptor_Success() {
        String result = LTIJacksonValidator.validateDescriptor(SAMPLE_DESCRIPTOR);
        assertEquals("Should return secure launch URL when available", 
                    "https://example.com/launch", result);
    }

    @Test
    public void testLTIJacksonValidator_ValidateDescriptor_SimpleDescriptor() {
        String result = LTIJacksonValidator.validateDescriptor(SIMPLE_DESCRIPTOR);
        assertEquals("Should return launch URL when no secure URL available", 
                    "http://simple.com/launch", result);
    }

    @Test
    public void testLTIJacksonValidator_ValidateDescriptor_SecureOnlyDescriptor() {
        String result = LTIJacksonValidator.validateDescriptor(SECURE_ONLY_DESCRIPTOR);
        assertEquals("Should return secure launch URL", 
                    "https://secure.com/launch", result);
    }

    @Test
    public void testLTIJacksonValidator_ValidateDescriptor_NullDescriptor() {
        String result = LTIJacksonValidator.validateDescriptor(null);
        assertNull("Should return null for null descriptor", result);
    }

    @Test
    public void testLTIJacksonValidator_ValidateDescriptor_NoLaunchURLs() {
        String noLaunchDescriptor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title>No Launch URLs</title>" +
                "</basic_lti_link>";
        
        String result = LTIJacksonValidator.validateDescriptor(noLaunchDescriptor);
        assertNull("Should return null when no launch URLs are present", result);
    }

    // Unit Tests for LTIJacksonExporter

    @Test
    public void testLTIJacksonExporter_PrepareForExport_Success() {
        String result = LTIJacksonExporter.prepareForExport(SAMPLE_DESCRIPTOR);
        
        assertNotNull("prepareForExport should return non-null result", result);
        assertTrue("Result should contain basic_lti_link", result.contains("basic_lti_link"));
        assertTrue("Result should contain title", result.contains("Test Tool"));
        assertTrue("Result should contain launch_url", result.contains("http://example.com/launch"));
        assertTrue("Result should contain secure_launch_url", result.contains("https://example.com/launch"));
    }

    @Test
    public void testLTIJacksonExporter_PrepareForExport_NullDescriptor() {
        String result = LTIJacksonExporter.prepareForExport(null);
        assertNull("Should return null for null descriptor", result);
    }

    // Comparison Tests: Refactored vs Original

    @Test
    public void testComparison_ParseDescriptor() {
        Map<String, String> refactoredLaunchInfo = new HashMap<>();
        Map<String, String> refactoredPostProp = new HashMap<>();
        boolean refactoredResult = LTIJacksonParser.parseDescriptor(refactoredLaunchInfo, refactoredPostProp, SAMPLE_DESCRIPTOR);
        
        Map<String, String> originalLaunchInfo = new HashMap<>();
        Map<String, String> originalPostProp = new HashMap<>();
        boolean originalResult = LTIUtil.parseDescriptorJackson(originalLaunchInfo, originalPostProp, SAMPLE_DESCRIPTOR);
        
        assertTrue("Refactored method should succeed", refactoredResult);
        assertTrue("Original method should succeed", originalResult);
        
        assertEquals("Launch URLs should match", 
                    originalLaunchInfo.get("launch_url"), 
                    refactoredLaunchInfo.get("launch_url"));
        assertEquals("Secure launch URLs should match", 
                    originalLaunchInfo.get("secure_launch_url"), 
                    refactoredLaunchInfo.get("secure_launch_url"));
        
        for (String key : originalPostProp.keySet()) {
            if (key.startsWith("custom_")) {
                assertEquals("Custom parameter " + key + " should match", 
                            originalPostProp.get(key), 
                            refactoredPostProp.get(key));
            }
        }
    }

    @Test
    public void testComparison_ValidateDescriptor() {
        String refactoredResult = LTIJacksonValidator.validateDescriptor(SAMPLE_DESCRIPTOR);
        String originalResult = LTIUtil.validateDescriptorJackson(SAMPLE_DESCRIPTOR);
        
        assertEquals("Validation results should match", originalResult, refactoredResult);
    }

    @Test
    public void testComparison_PrepareForExport() {
        String refactoredResult = LTIJacksonExporter.prepareForExport(SAMPLE_DESCRIPTOR);
        String originalResult = LTIUtil.prepareForExportJackson(SAMPLE_DESCRIPTOR);
        
        assertNotNull("Refactored result should not be null", refactoredResult);
        assertNotNull("Original result should not be null", originalResult);
        
        // Both should contain the same basic elements
        assertTrue("Refactored result should contain basic_lti_link", refactoredResult.contains("basic_lti_link"));
        assertTrue("Original result should contain basic_lti_link", originalResult.contains("basic_lti_link"));
    }

    // Edge Case Tests

    @Test
    public void testLTIJacksonParser_ParseDescriptor_EmptyCustomParameters() {
        String descriptorWithEmptyCustom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title>Empty Custom</title>" +
                "  <launch_url>http://example.com/launch</launch_url>" +
                "  <custom></custom>" +
                "</basic_lti_link>";
        
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, descriptorWithEmptyCustom);
        assertTrue("Should handle empty custom section", result);
        assertEquals("http://example.com/launch", launchInfo.get("launch_url"));
    }

    @Test
    public void testLTIJacksonParser_ParseDescriptor_MissingValues() {
        String descriptorWithMissingValues = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title></title>" +
                "  <launch_url>http://example.com/launch</launch_url>" +
                "  <custom>" +
                "    <parameter key=\"key1\"></parameter>" +
                "    <parameter key=\"\">value2</parameter>" +
                "  </custom>" +
                "</basic_lti_link>";
        
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, descriptorWithMissingValues);
        assertTrue("Should handle missing values gracefully", result);
        assertEquals("http://example.com/launch", launchInfo.get("launch_url"));
        
        assertFalse("Should not add parameter with empty key", postProp.containsKey("custom_"));
        assertFalse("Should not add parameter with empty value", postProp.containsKey("custom_key1"));
    }

    @Test
    public void testLTIJacksonParser_ParseDescriptor_SpecialCharacters() {
        String descriptorWithSpecialChars = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<basic_lti_link xmlns=\"http://www.imsglobal.org/xsd/imsbasiclti_v1p0\">" +
                "  <title>Special &amp; Characters</title>" +
                "  <launch_url>http://example.com/launch</launch_url>" +
                "  <custom>" +
                "    <parameter key=\"key:with:colons\">value with spaces</parameter>" +
                "  </custom>" +
                "</basic_lti_link>";
        
        boolean result = LTIJacksonParser.parseDescriptor(launchInfo, postProp, descriptorWithSpecialChars);
        assertTrue("Should handle special characters", result);
        assertEquals("Special & Characters", launchInfo.get("title"));
        assertEquals("value with spaces", postProp.get("custom_key_with_colons"));
    }

    // Utility Method Tests

    @Test
    public void testLTIJacksonParser_MapKeyName() {
        assertEquals("simple_key", LTIJacksonParser.mapKeyName("simple_key"));
        assertEquals("key_with_underscores", LTIJacksonParser.mapKeyName("key-with-underscores"));
        assertEquals("key_with_colons", LTIJacksonParser.mapKeyName("key:with:colons"));
        assertEquals("key_with_spaces", LTIJacksonParser.mapKeyName("key with spaces"));
        assertEquals("key_with_special_chars", LTIJacksonParser.mapKeyName("key@with#special$chars"));
        assertNull("Should return null for null input", LTIJacksonParser.mapKeyName(null));
        assertNull("Should return null for empty input", LTIJacksonParser.mapKeyName(""));
        assertNull("Should return null for whitespace input", LTIJacksonParser.mapKeyName("   "));
    }

    @Test
    public void testLTIJacksonParser_SetProperty() {
        Map<String, String> testMap = new HashMap<>();
        
        LTIJacksonParser.setProperty(testMap, "key1", "value1");
        assertEquals("value1", testMap.get("key1"));
        
        LTIJacksonParser.setProperty(testMap, "key2", "");
        assertFalse("Should not add empty value", testMap.containsKey("key2"));
        
        LTIJacksonParser.setProperty(testMap, "key3", null);
        assertFalse("Should not add null value", testMap.containsKey("key3"));
        
        LTIJacksonParser.setProperty(testMap, "key4", "   ");
        assertFalse("Should not add whitespace-only value", testMap.containsKey("key4"));
    }

    // Performance Test

    @Test
    public void testPerformanceComparison() {
        for (int i = 0; i < 10; i++) {
            LTIJacksonParser.parseDescriptor(launchInfo, postProp, SAMPLE_DESCRIPTOR);
            LTIUtil.parseDescriptorJackson(launchInfo, postProp, SAMPLE_DESCRIPTOR);
        }
        
        long refactoredStart = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            Map<String, String> testLaunchInfo = new HashMap<>();
            Map<String, String> testPostProp = new HashMap<>();
            LTIJacksonParser.parseDescriptor(testLaunchInfo, testPostProp, SAMPLE_DESCRIPTOR);
        }
        long refactoredTime = System.nanoTime() - refactoredStart;
        
        long originalStart = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            Map<String, String> testLaunchInfo = new HashMap<>();
            Map<String, String> testPostProp = new HashMap<>();
            LTIUtil.parseDescriptorJackson(testLaunchInfo, testPostProp, SAMPLE_DESCRIPTOR);
        }
        long originalTime = System.nanoTime() - originalStart;
        
        System.out.println("Refactored parsing time: " + refactoredTime + " ns");
        System.out.println("Original parsing time: " + originalTime + " ns");
        
        assertTrue("Refactored method should complete", refactoredTime > 0);
        assertTrue("Original method should complete", originalTime > 0);
    }
}

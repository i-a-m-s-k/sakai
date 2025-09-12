package org.tsugi.lti;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.tsugi.lti.objects.LTIDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson-based LTI Descriptor Parser
 * 
 * This class provides functionality to parse LTI descriptor XML using Jackson XML mapping.
 * It extracts launch URLs, custom parameters, vendor information, and other LTI tool metadata.
 */
public class LTIJacksonParser {
    
    private static final Logger log = LoggerFactory.getLogger(LTIJacksonParser.class);

    /**
     * Parse LTI descriptor using Jackson-based LTIDescriptor object.
     * 
     * @param launch_info Variable is mutated by this method.
     * @param postProp Variable is mutated by this method.
     * @param descriptor The XML descriptor string to parse
     * @return true if parsing was successful, false otherwise
     */
    public static boolean parseDescriptor(Map<String, String> launch_info,
            Map<String, String> postProp, String descriptor) {
        
        if (descriptor == null || descriptor.trim().isEmpty()) {
            log.warn("Descriptor is null or empty in parseDescriptor");
            return false;
        }

        try {
            XmlMapper mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setDefaultUseWrapper(false);

            LTIDescriptor ltiDescriptor = mapper.readValue(descriptor.trim(), LTIDescriptor.class);
            
            if (ltiDescriptor == null) {
                log.warn("Failed to parse LTI descriptor with Jackson");
                return false;
            }
            String launchUrl = StringUtils.trimToNull(ltiDescriptor.getLaunchUrl());
            String secureLaunchUrl = StringUtils.trimToNull(ltiDescriptor.getSecureLaunchUrl());
            
            if (launchUrl == null && secureLaunchUrl == null) {
                log.warn("No launch URL found in descriptor");
                return false;
            }

            setProperty(launch_info, "launch_url", launchUrl);
            setProperty(launch_info, "secure_launch_url", secureLaunchUrl);

            if (ltiDescriptor.getCustom() != null && ltiDescriptor.getCustom().getParameters() != null) {
                for (LTIDescriptor.Parameter param : ltiDescriptor.getCustom().getParameters()) {
                    if (param.getKey() != null && param.getValue() != null) {
                        String key = "custom_" + mapKeyName(param.getKey());
                        log.debug("key={} val={}", key, param.getValue());
                        postProp.put(key, param.getValue());
                    }
                }
            }

            if (ltiDescriptor.getExtensions() != null) {
                for (LTIDescriptor.Extension extension : ltiDescriptor.getExtensions()) {
                    if (extension.getParameters() != null) {
                        for (LTIDescriptor.Parameter param : extension.getParameters()) {
                            if (param.getKey() != null && param.getValue() != null) {
                                String key = "custom_" + mapKeyName(param.getKey());
                                log.debug("extension key={} val={}", key, param.getValue());
                                postProp.put(key, param.getValue());
                            }
                        }
                    }
                }
            }

            if (ltiDescriptor.getVendor() != null) {
                setProperty(launch_info, "vendor_code", ltiDescriptor.getVendor().getCode());
                setProperty(launch_info, "vendor_name", ltiDescriptor.getVendor().getName());
                setProperty(launch_info, "vendor_description", ltiDescriptor.getVendor().getDescription());
                setProperty(launch_info, "vendor_url", ltiDescriptor.getVendor().getUrl());
                
                if (ltiDescriptor.getVendor().getContact() != null) {
                    setProperty(launch_info, "vendor_contact_email", ltiDescriptor.getVendor().getContact().getEmail());
                }
            }

            setProperty(launch_info, "title", ltiDescriptor.getTitle());
            setProperty(launch_info, "description", ltiDescriptor.getDescription());
            setProperty(launch_info, "icon", ltiDescriptor.getIcon());
            setProperty(launch_info, "secure_icon", ltiDescriptor.getSecureIcon());

            log.debug("Successfully parsed LTI descriptor with Jackson");
            return true;

        } catch (Exception e) {
            log.warn("LTIJacksonParser exception parsing LTI descriptor with Jackson: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Maps a key name to a safe format by converting to lowercase and replacing
     * non-alphanumeric characters with underscores.
     * 
     * @param keyname The key name to map
     * @return The mapped key name, or null if input is null or empty
     */
    public static String mapKeyName(String keyname) {
        StringBuffer sb = new StringBuffer();
        if (keyname == null)
            return null;
        keyname = keyname.trim();
        if (keyname.length() < 1)
            return null;
        for (int i = 0; i < keyname.length(); i++) {
            Character ch = Character.toLowerCase(keyname.charAt(i));
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                sb.append(ch);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    /**
     * Mutates the passed Map<String, String> map variable. Puts the key,value
     * into the Map if the value is not null and is not blank (not empty or whitespace-only).
     *
     * @param map Variable is mutated by this method.
     * @param key
     * @param value
     */
    public static void setProperty(final Map<String, String> map,
            final String key, final String value) {
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }
}

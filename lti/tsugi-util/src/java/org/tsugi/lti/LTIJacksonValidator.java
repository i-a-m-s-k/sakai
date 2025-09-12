package org.tsugi.lti;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.tsugi.lti.objects.LTIDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson-based LTI Descriptor Validator
 * 
 * This class provides functionality to validate LTI descriptor XML using Jackson XML mapping.
 * It extracts and validates launch URLs from LTI tool descriptors.
 */
public class LTIJacksonValidator {
    
    private static final Logger log = LoggerFactory.getLogger(LTIJacksonValidator.class);

    /**
     * Validate LTI descriptor using Jackson-based LTIDescriptor object.
     *
     * @param descriptor The XML descriptor string to validate
     * @return The launch URL if valid, null otherwise
     */
    public static String validateDescriptor(String descriptor) {
        if (descriptor == null || descriptor.trim().isEmpty()) {
            return null;
        }
        
        if (descriptor.indexOf("<basic_lti_link") < 0) {
            return null;
        }

        try {
            XmlMapper mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            LTIDescriptor ltiDescriptor = mapper.readValue(descriptor.trim(), LTIDescriptor.class);
            
            if (ltiDescriptor == null) {
                return null;
            }

            String secureLaunchUrl = StringUtils.trimToNull(ltiDescriptor.getSecureLaunchUrl());
            if (secureLaunchUrl != null && !secureLaunchUrl.isEmpty()) {
                return secureLaunchUrl;
            }

            String launchUrl = StringUtils.trimToNull(ltiDescriptor.getLaunchUrl());
            if (launchUrl != null && !launchUrl.isEmpty()) {
                return launchUrl;
            }

            return null;

        } catch (Exception e) {
            log.warn("LTIJacksonValidator exception validating LTI descriptor with Jackson: {}", e.getMessage(), e);
            return null;
        }
    }
}

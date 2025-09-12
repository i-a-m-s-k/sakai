package org.tsugi.lti;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.tsugi.lti.objects.LTIDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson-based LTI Descriptor Exporter
 * 
 * This class provides functionality to prepare LTI descriptor XML for export using Jackson XML mapping.
 * It removes sensitive information like keys and secrets from LTI tool descriptors.
 */
public class LTIJacksonExporter {
    
    private static final Logger log = LoggerFactory.getLogger(LTIJacksonExporter.class);

    /**
     * Prepare LTI descriptor for export using Jackson-based LTIDescriptor object.
     * This removes sensitive information like keys and secrets.
     *
     * @param descriptor The XML descriptor string to prepare for export
     * @return The cleaned descriptor XML, or null if parsing failed
     */
    public static String prepareForExport(String descriptor) {
        if (descriptor == null || descriptor.trim().isEmpty()) {
            return null;
        }

        try {
            XmlMapper mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            LTIDescriptor ltiDescriptor = mapper.readValue(descriptor.trim(), LTIDescriptor.class);
            
            if (ltiDescriptor == null) {
                log.warn("Unable to parse XML in prepareForExport");
                return null;
            }

            LTIDescriptor cleanDescriptor = new LTIDescriptor();
            
            cleanDescriptor.setTitle(ltiDescriptor.getTitle());
            cleanDescriptor.setDescription(ltiDescriptor.getDescription());
            cleanDescriptor.setLaunchUrl(ltiDescriptor.getLaunchUrl());
            cleanDescriptor.setSecureLaunchUrl(ltiDescriptor.getSecureLaunchUrl());
            cleanDescriptor.setIcon(ltiDescriptor.getIcon());
            cleanDescriptor.setSecureIcon(ltiDescriptor.getSecureIcon());
            cleanDescriptor.setCartridgeIcon(ltiDescriptor.getCartridgeIcon());
            cleanDescriptor.setVendor(ltiDescriptor.getVendor());
            cleanDescriptor.setCustom(ltiDescriptor.getCustom());
            cleanDescriptor.setExtensions(ltiDescriptor.getExtensions());

            String cleanXml = mapper.writeValueAsString(cleanDescriptor);
            return cleanXml;

        } catch (Exception e) {
            log.warn("LTIJacksonExporter exception preparing LTI descriptor for export with Jackson: {}", e.getMessage(), e);
            return null;
        }
    }
}

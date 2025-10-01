package org.tsugi.lti.oauth;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.signature.OAuthSignatureMethod;

import java.io.IOException;
import java.util.Map;

/**
 * Simple OAuth service for signing requests
 */
public class OAuthService {

    public OAuthMessage signProperties(Map<String, String> properties, String url, String method, 
                                     String oauthKey, String oauthSecret) throws IOException {
        try {
            // Create OAuth consumer
            OAuthConsumer consumer = new OAuthConsumer(null, oauthKey, oauthSecret, null);
            
            // Create OAuth message
            OAuthMessage message = new OAuthMessage(method, url, properties.entrySet());
            // Prepare accessor for signer
            OAuthAccessor accessor = new OAuthAccessor(consumer);
            // Create signer based on message's oauth_signature_method (default HMAC-SHA1 if absent)
            if (message.getSignatureMethod() == null) {
                message.addParameter(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
            }
            OAuthSignatureMethod signer = OAuthSignatureMethod.newSigner(message, accessor);
            signer.sign(message);
            
            return message;
        } catch (Exception e) {
            throw new IOException("Failed to sign OAuth request", e);
        }
    }
}

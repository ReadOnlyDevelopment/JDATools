
package com.readonlydev.oauth2.session;

import java.time.OffsetDateTime;
import java.util.HashMap;

import com.readonlydev.oauth2.Scope;
import com.readonlydev.oauth2.session.DefaultSessionController.DefaultSession;

/**
 * The default {@link com.readonlydev.oauth2.session.SessionController
 * SessionController} implementation.
 */
public class DefaultSessionController
        implements SessionController<DefaultSession> {

    private final HashMap<String, DefaultSession> sessions = new HashMap<>();

    @Override
    public DefaultSession getSession(String identifier) {
        return sessions.get(identifier);
    }

    @Override
    public DefaultSession createSession(SessionData data) {
        DefaultSession created = new DefaultSession(data);
        sessions.put(data.getIdentifier(), created);
        return created;
    }

    public class DefaultSession implements Session {

        private final String         accessToken, refreshToken, tokenType;

        private final OffsetDateTime expiration;

        private final Scope[]        scopes;

        private DefaultSession(String accessToken, String refreshToken,
                String tokenType, OffsetDateTime expiration, Scope[] scopes) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.tokenType = tokenType;
            this.expiration = expiration;
            this.scopes = scopes;
        }

        private DefaultSession(SessionData data) {
            this(data.getAccessToken(), data.getRefreshToken(),
                data.getTokenType(), data.getExpiration(), data.getScopes());
        }

        @Override
        public String getAccessToken() {
            return accessToken;
        }

        @Override
        public String getRefreshToken() {
            return refreshToken;
        }

        @Override
        public Scope[] getScopes() {
            return scopes;
        }

        @Override
        public String getTokenType() {
            return tokenType;
        }

        @Override
        public OffsetDateTime getExpiration() {
            return expiration;
        }
    }
}

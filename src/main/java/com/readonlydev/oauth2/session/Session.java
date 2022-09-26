
package com.readonlydev.oauth2.session;

import java.time.OffsetDateTime;

import com.readonlydev.oauth2.Scope;

/**
 * Implementable data type used to allow access to data regarding OAuth2
 * sessions.
 * <p>
 * This can be used with a proper
 * {@link com.readonlydev.oauth2.OAuth2Client OAuth2Client} to get information
 * on the logged in {@link com.readonlydev.oauth2.entities.OAuth2User User}, as
 * well as {@link com.readonlydev.oauth2.entities.OAuth2Guild Guilds} they are
 * on.
 */
public interface Session
{

	/**
	 * Gets the session's access token.
	 *
	 * @return The session's access token.
	 */
	String getAccessToken();

	/**
	 * Gets the session's refresh token.
	 *
	 * @return The session's refresh token.
	 */
	String getRefreshToken();

	/**
	 * Gets the session's {@link com.readonlydev.oauth2.Scope Scopes}.
	 *
	 * @return The session's Scopes.
	 */
	Scope[] getScopes();

	/**
	 * Gets the session's token type.
	 *
	 * @return The session's token type.
	 */
	String getTokenType();

	/**
	 * Gets the session's expiration time.
	 *
	 * @return The session's expiration time.
	 */
	OffsetDateTime getExpiration();
}


package com.readonlydev.oauth2.state;

/**
 * Implementable state controller, used for registering states and generating
 * redirect URIs using them.
 * <p>
 * Naturally, states should be unique, and
 * attempting to generate a redirect using a previously used state should return
 * {@code null} instead of a new redirect URI.
 */
public interface StateController
{

	/**
	 * Generates a new state string using the provided redirect URI.
	 * 
	 * @param redirectUri
	 *            The redirect URI that will be used with this state.
	 *
	 * @return The state string.
	 */
	String generateNewState(String redirectUri);

	/**
	 * Consumes a state to get the corresponding redirect URI.
	 * <p>
	 * Once this
	 * method is called for a specific state, it should return null for all
	 * future calls of that same state.
	 * 
	 * @param state
	 *            The state.
	 *
	 * @return The redirect URI, or {@code null} if the state does not exist.
	 */
	String consumeState(String state);
}

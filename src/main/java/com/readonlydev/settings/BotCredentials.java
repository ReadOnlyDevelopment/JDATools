
package com.readonlydev.settings;

import lombok.Data;

@Data
public final class BotCredentials {

    private String token;

    private String clientId;

    private String secret;
}

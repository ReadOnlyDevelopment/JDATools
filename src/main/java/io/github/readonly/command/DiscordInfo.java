package io.github.readonly.command;

import lombok.Data;
import net.dv8tion.jda.api.entities.User;

@Data
public class DiscordInfo {
	private String username;
	private String id;

	public static DiscordInfo of(User user)
	{
		var info = new DiscordInfo();
		info.setId(user.getId());
		info.setUsername(user.getName());
		return info;
	}
}
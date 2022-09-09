package com.readonlydev.command.ctx;

import com.readonlydev.api.CooldownScope;
import com.readonlydev.command.client.Client;
import com.readonlydev.command.operation.UserInteraction;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

/**
 * Middleware for child context menu types. Anything that extends this class will inherit the following options.
 */
public abstract class ContextMenu extends UserInteraction
{
    /**
     * The name of the command. This appears in the context menu.
     * Can be 1-32 characters long. Spaces are allowed.
     * @see CommandData#setName(String)
     */
    protected String name = "null";

    /**
     * Gets the {@link ContextMenu ContextMenu.name} for the Context Menu.
     *
     * @return The name for the Context Menu.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the type of context menu.
     *
     * @return the type
     */
    public Command.Type getType()
    {
        if (this instanceof MessageContextMenu) {
            return Command.Type.MESSAGE;
        } else if (this instanceof UserContextMenu) {
            return Command.Type.USER;
        } else {
            return Command.Type.UNKNOWN;
        }
    }

    /**
     * Gets the proper cooldown key for this Command under the provided {@link GenericCommandInteractionEvent}.
     *
     * @param event The ContextMenuEvent to generate the cooldown for.
     *
     * @return A String key to use when applying a cooldown.
     */
    public String getCooldownKey(GenericCommandInteractionEvent event)
    {
        switch (cooldownScope)
        {
            case USER:         return cooldownScope.genKey(name,event.getUser().getIdLong());
            case USER_GUILD:   return event.getGuild()!=null ? cooldownScope.genKey(name,event.getUser().getIdLong(),event.getGuild().getIdLong()) :
                CooldownScope.USER_CHANNEL.genKey(name,event.getUser().getIdLong(), event.getChannel().getIdLong());
            case USER_CHANNEL: return cooldownScope.genKey(name,event.getUser().getIdLong(),event.getChannel().getIdLong());
            case GUILD:        return event.getGuild()!=null ? cooldownScope.genKey(name,event.getGuild().getIdLong()) :
                CooldownScope.CHANNEL.genKey(name,event.getChannel().getIdLong());
            case CHANNEL:      return cooldownScope.genKey(name,event.getChannel().getIdLong());
            case SHARD:        return event.getJDA().getShardInfo() != JDA.ShardInfo.SINGLE ? cooldownScope.genKey(name, event.getJDA().getShardInfo().getShardId()) :
                CooldownScope.GLOBAL.genKey(name, 0);
            case USER_SHARD:   return event.getJDA().getShardInfo() != JDA.ShardInfo.SINGLE ? cooldownScope.genKey(name,event.getUser().getIdLong(),event.getJDA().getShardInfo().getShardId()) :
                CooldownScope.USER.genKey(name, event.getUser().getIdLong());
            case GLOBAL:       return cooldownScope.genKey(name, 0);
            default:           return "";
        }
    }

    /**
     * Gets an error message for this Context Menu under the provided {@link GenericCommandInteractionEvent}.
     *
     * @param  event
     *         The event to generate the error message for.
     * @param  remaining
     *         The remaining number of seconds a context menu is on cooldown for.
     * @param client the client
     *
     * @return A String error message for this menu if {@code remaining > 0},
     *         else {@code null}.
     */
    public String getCooldownError(GenericCommandInteractionEvent event, int remaining, Client client)
    {
        if(remaining<=0) {
            return null;
        }
        String front = client.getWarning()+" That command is on cooldown for "+remaining+" more seconds";
        if(cooldownScope.equals(CooldownScope.USER)) {
            return front+"!";
        } else if(cooldownScope.equals(CooldownScope.USER_GUILD) && (event.getGuild()==null)) {
            return front+" "+CooldownScope.USER_CHANNEL.getErrorSpecification()+"!";
        } else if(cooldownScope.equals(CooldownScope.GUILD) && (event.getGuild()==null)) {
            return front+" "+CooldownScope.CHANNEL.getErrorSpecification()+"!";
        } else {
            return front+" "+cooldownScope.getErrorSpecification()+"!";
        }
    }

    /**
     * Builds CommandData for the ContextMenu upsert.
     * This code is executed when we need to upsert the menu.
     *
     * Useful for manual upserting.
     *
     * @return the built command data
     */
    public CommandData buildCommandData()
    {
        // Make the command data
        CommandData data = Commands.context(getType(), name);

        if (this.userPermissions == null) {
            data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        } else {
            data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(this.userPermissions));
        }

        data.setGuildOnly(this.guildOnly);

        return data;
    }
}

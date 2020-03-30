package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class AvatarCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        List<User> mentionedUsers = ctx.getMessage().getMentionedUsers();
        User user = mentionedUsers.isEmpty() ? ctx.getAuthor() : mentionedUsers.get(0);
        Color color = ctx.getMember().getColor();
        if (color == null) color = new Color(0);
        MessageEmbed embed = new EmbedBuilder().setTitle(user.getName() + "'s avatar!").setImage(user.getAvatarUrl()).setColor(color).build();
        CommandManager.LOGGER.info("Getting " + user.getAsTag() + "'s avatar");
        ctx.getChannel().sendMessage(embed).queue();
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getHelp() {
        return "Gets a user's avatar\n"
                + "Usage: `" + getPrefixedName() + " [@user]`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("getavatar", "profile", "getprofile");
    }
}

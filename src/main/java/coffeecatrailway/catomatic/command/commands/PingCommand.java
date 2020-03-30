package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
import coffeecatrailway.catomatic.Config;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        if (args.isEmpty()) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        int pings;
        try {
            pings = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            channel.sendMessage(getHelp()).queue();
            return;
        }
        if (pings < 1) pings = 1;
        if (pings > Integer.parseInt(Config.get("maxpings"))) {
            channel.sendMessage("You can only ping a user up yo " + Config.get("maxpings") + " times").queue();
            return;
        }

        List<User> mentionedUsers = ctx.getMessage().getMentionedUsers();
        User user = mentionedUsers.isEmpty() ? ctx.getAuthor() : mentionedUsers.get(0);
        CommandManager.LOGGER.info(ctx.getAuthor().getAsTag() + " is pinning " + user.getAsTag() + " " + pings + " times...");
        StringBuilder text = new StringBuilder();
        if (args.size() >= 3)
            for (int i = 2; i < args.size(); i++)
                text.append(args.get(i)).append(" ");
        for (int i = 0; i < pings; i++)
            channel.sendMessage("#" + (i + 1) + " <@" + user.getId() + "> " + text.toString()).queue();
        CommandManager.LOGGER.info(user.getAsTag() + " has been pinged " + pings + " times...");
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Pings a user some number of times\n"
                + "Usage: `" + getPrefixedName() + " <ping amount | MAX:" + Config.get("maxpings") + "> [person] [message]`";
    }
}

package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
import coffeecatrailway.catomatic.Config;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        if (args.isEmpty()) {
            EmbedBuilder builder = new EmbedBuilder().setTitle("List of available commands:").setColor(new Color(0x00FF00));
            manager.getCommands().forEach(cmd -> builder.appendDescription(Config.get("prefix") + cmd.getName() + "\n" + cmd.getHelp() + "\n\n"));
            channel.sendMessage(builder.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);
        if (command == null) {
            channel.sendMessage("Nothing found for " + search).queue();
            return;
        }

        if (!command.getAliases().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String alias : command.getAliases())
                builder.append(alias).append(", ");
            String aliases = builder.toString();
            channel.sendMessage(command.getHelp() + "\n"
                    + "Aliases: " + aliases.substring(0, aliases.length() - 2) + "").queue();
        } else
            channel.sendMessage(command.getHelp()).queue();
        CommandManager.LOGGER.info(ctx.getAuthor().getAsTag() + " needs help...");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the a list of commands available\n"
                + "Usage `" + getPrefixedName() + " [command]`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "cmds", "commandlist");
    }
}

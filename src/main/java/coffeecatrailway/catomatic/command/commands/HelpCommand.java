package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
import coffeecatrailway.catomatic.Config;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
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
        CommandManager.LOGGER.info(ctx.getAuthor().getAsTag() + " needs help...");

        if (args.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("List of available commands:").setColor(new Color(0x00FF00));

            StringBuilder builder = new StringBuilder().append("Other categories: ");
            Arrays.stream(HelpCategory.values()).map(HelpCategory::getName).forEach(name -> {
                if (!name.equals(HelpCategory.NORMAL.getName()))
                    builder.append("**").append(name).append("**, ");
            });
            String builderString = builder.toString();
            builderString = builderString.substring(0, builderString.length() - 2);
            embed.appendDescription(builderString + "\n\n");

            channel.sendMessage(getCategory(embed, HelpCategory.NORMAL).build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);
        if (command == null) {
            if (Arrays.stream(HelpCategory.values()).anyMatch(cat -> cat.getName().toLowerCase().equals(search.toLowerCase()))) {
                HelpCategory category = HelpCategory.valueOf(search.toUpperCase());
                EmbedBuilder embed = new EmbedBuilder().setTitle(String.format("List of available commands for **%s**:", category.getName())).setColor(new Color(0x00FF00));
                channel.sendMessage(getCategory(embed, category).build()).queue();
            } else {
                channel.sendMessage("Nothing found for " + search).queue();
            }
            return;
        }

        if (!command.getAliases().isEmpty()) {
            channel.sendMessage(command.getHelp() + "\n"
                    + "Aliases: " + getAliases(command) + "").queue();
        } else
            channel.sendMessage(command.getHelp()).queue();
    }

    private EmbedBuilder getCategory(EmbedBuilder embed, HelpCategory category) {
        manager.getCommands().forEach((id, command) -> {
            if (command.getCategory() == category) {
                embed.appendDescription(Config.get("prefix") + id);
                embed.appendDescription("\n" + command.getHelp());
                if (!command.getAliases().isEmpty())
                    embed.appendDescription("\n" + getAliases(command));
                embed.appendDescription("\n\n");
            }
        });
        return embed;
    }

    private String getAliases(ICommand command) {
        StringBuilder builder = new StringBuilder();
        for (String alias : command.getAliases())
            builder.append(alias).append(", ");
        String aliases = builder.toString();
        return aliases.substring(0, aliases.length() - 2);
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

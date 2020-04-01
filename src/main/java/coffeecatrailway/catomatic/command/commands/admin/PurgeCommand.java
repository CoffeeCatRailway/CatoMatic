package coffeecatrailway.catomatic.command.commands.admin;

import coffeecatrailway.catomatic.Config;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author CoffeeCatRailway
 * Created: 1/04/2020
 */
public class PurgeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("You need the `Manage Messages` permission to use this command").queue();

            return;
        }

        if (!ctx.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("I need the `Manage Messages` permission for this command").queue();

            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            channel.sendMessageFormat("`%s` is not a valid number", args.get(0)).queue();
            return;
        }

        if (amount < Integer.parseInt(Config.get("minpurge")) || amount > Integer.parseInt(Config.get("maxpurge"))) {
            channel.sendMessageFormat("Amount must be at least %s and at most %s", Config.get("minpurge"), Config.get("maxpurge")).queue();
            return;
        }

        channel.getIterableHistory().takeAsync(amount).thenApplyAsync(messages -> {
            List<Message> goodMessages = messages.stream().filter(message -> message.getTimeCreated().isBefore(OffsetDateTime.now().plus(2, ChronoUnit.WEEKS))).collect(Collectors.toList());
            channel.purgeMessages(goodMessages);
            return goodMessages.size();
        }).whenCompleteAsync((count, thr) ->
            channel.sendMessageFormat("Deleted `%d` messages", count).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS))
        ).exceptionally(thr -> {
            String cause = "";
            if (thr.getCause() != null)
                cause = " caused by: " + thr.getCause().getMessage();
            channel.sendMessageFormat("Error: %s%s", thr.getMessage(), cause).queue();
            return 0;
        });
    }

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public String getHelp() {
        return "Clears the chat with the specified amount of messages\n"
                + "Usage: `" + getPrefixedName() + " <amount>`";
    }

    @Override
    public HelpCategory getCategory() {
        return HelpCategory.ADMIN;
    }
}

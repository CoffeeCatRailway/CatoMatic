package coffeecatrailway.catomatic.command.commands.admin;

import coffeecatrailway.catomatic.CommandManager;
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

    private final int maxPurge = 100, minPurge = 2;

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

        if (amount < minPurge || amount > maxPurge) {
            channel.sendMessageFormat("Amount must be at least %s and at most %s", minPurge, maxPurge).queue();
            return;
        }

        channel.getIterableHistory().takeAsync(amount).thenApplyAsync(messages -> {
            List<Message> goodMessages = messages.stream().filter(message -> message.getTimeCreated().isBefore(OffsetDateTime.now().plus(2, ChronoUnit.WEEKS))).collect(Collectors.toList());
            channel.purgeMessages(goodMessages);
            return goodMessages.size();
        }).whenCompleteAsync((count, thr) -> {
            String formatted = String.format("Deleted `%d` messages", count);
            channel.sendMessage(formatted).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            CommandManager.LOGGER.info(formatted);
        }).exceptionally(thr -> {
            String cause = "";
            if (thr.getCause() != null)
                cause = " caused by: " + thr.getCause().getMessage();
            String formatted = String.format("Error: %s%s", thr.getMessage(), cause);
            channel.sendMessage(formatted).queue();
            CommandManager.LOGGER.info(formatted);
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

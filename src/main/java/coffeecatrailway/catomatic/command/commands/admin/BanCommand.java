package coffeecatrailway.catomatic.command.commands.admin;

import coffeecatrailway.catomatic.CommandManager;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 1/04/2020
 */
public class BanCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Message message = ctx.getMessage();
        Member member = ctx.getMember();
        List<String> args = ctx.getArgs();

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        Member target = message.getMentionedMembers().get(0);
        if (!member.canInteract(target) || !member.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You are missing permission to ban this member!").queue();
            return;
        }

        Member selfMember = ctx.getSelfMember();
        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I am missing permissions to ban that member!").queue();
            return;
        }

        String reason = String.join(" ", args.subList(1, args.size()));
        ctx.getGuild().ban(target, 1).reason(reason)
                .queue(
                        (__) -> channel.sendMessage("Ban was successful!").queue(),
                        (error) -> channel.sendMessageFormat("Could not kick %s", error.getMessage()).queue()
                );
        CommandManager.LOGGER.info("Banning " + target.getUser().getAsTag() + " from " + ctx.getGuild().getName());
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getHelp() {
        return "Bans a user from the server\n"
                + "Usage: `" + getPrefixedName() + " <user> <reason>`";
    }

    @Override
    public HelpCategory getCategory() {
        return HelpCategory.ADMIN;
    }
}

package coffeecatrailway.catomatic.command.commands.admin;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CoffeeCatRailway
 * Created: 1/04/2020
 */
public class UnbanCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Guild guild = ctx.getGuild();
        List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        if (!ctx.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I need the Ban Members permission to unban members").queue();
            return;
        }

        if (!guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I need the Ban Members permission to unban members").queue();
            return;
        }

        String argsJoined = String.join(" ", args);
        guild.retrieveBanList().queue(bans -> {
            List<User> goodUsers = bans.stream().filter(ban -> isCorrectUser(ban, argsJoined)).map(Guild.Ban::getUser).collect(Collectors.toList());
            if (goodUsers.isEmpty()) {
                channel.sendMessage("This user is not banned").queue();
                return;
            }

            User target = goodUsers.get(0);
            String mod = String.format("%#s", ctx.getAuthor());
            String bannedUser = String.format("%#s", target);

            guild.unban(target).reason("Unbanned by " + mod).queue();
            channel.sendMessage("User " + bannedUser + " has been unbanned by " + mod).queue();
        });
    }

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();
        return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg)
                || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getHelp() {
        return "Unbans a member from the server\n"
                + "Usage: `" + getPrefixedName() + " <username/user id/username#disc>`";
    }

    @Override
    public HelpCategory getCategory() {
        return HelpCategory.ADMIN;
    }
}

package coffeecatrailway.catomatic.command.commands.admin;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;

import java.time.format.DateTimeFormatter;

/**
 * @author CoffeeCatRailway
 * Created: 31/03/2020
 */
public class ServerInfoCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        Guild guild = ctx.getGuild();

        String generalInfo = String.format(
                "**Owner**: <@%s>\n**Region**: %s\n**Creation Date**: %s\n**Verification Level**: %s",
                guild.getOwnerId(),
                guild.getRegion().getName(),
                guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                convertVerificationLevel(guild.getVerificationLevel())
        );

        String memberInfo = String.format(
                "**Total Roles**: %s\n**Total Members**: %s\n**Online Members**: %s\n**Offline Members**: %s\n**Bot Count**: %s",
                guild.getRoleCache().size(),
                guild.getMemberCache().size(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.ONLINE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.OFFLINE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getUser().isBot()).count()
        );

        EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle("Server info for " + guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("General Info", generalInfo, false)
                .addField("Role And Member Counts", memberInfo, false);

        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "serverinfo";
    }

    @Override
    public String getHelp() {
        return "Shows information about the server";
    }

    @Override
    public HelpCategory getCategory() {
        return HelpCategory.ADMIN;
    }

    private String convertVerificationLevel(Guild.VerificationLevel lvl) {
        String[] names = lvl.name().toLowerCase().split("_");
        StringBuilder out = new StringBuilder();

        for (String name : names)
            out.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).append(" ");
        return out.toString().trim();
    }
}

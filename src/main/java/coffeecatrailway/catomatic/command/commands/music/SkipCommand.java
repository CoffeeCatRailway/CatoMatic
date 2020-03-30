package coffeecatrailway.catomatic.command.commands.music;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import coffeecatrailway.catomatic.music.GuildMusicManager;
import coffeecatrailway.catomatic.music.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class SkipCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());

        if (musicManager.player.getPlayingTrack() == null) {
            channel.sendMessage("Nothing is currently playing").queue();
            return;
        }
        musicManager.scheduler.nextTrack();
        channel.sendMessage("Skipping the current track").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skip current song";
    }
}

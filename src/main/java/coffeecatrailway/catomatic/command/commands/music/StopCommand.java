package coffeecatrailway.catomatic.command.commands.music;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import coffeecatrailway.catomatic.music.GuildMusicManager;
import coffeecatrailway.catomatic.music.PlayerManager;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class StopCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        stop(ctx);
        ctx.getChannel().sendMessage("Stopped!").queue();
    }

    public static void stop(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stop the current song playing";
    }
}

package coffeecatrailway.catomatic.command.commands.music;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import coffeecatrailway.catomatic.music.GuildMusicManager;
import coffeecatrailway.catomatic.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class LeaveCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            channel.sendMessage("I'm not connected to a voice channel").queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            channel.sendMessage("You have to be in the same voice channel as me to use this").queue();
            return;
        }

        StopCommand.stop(ctx);

        audioManager.closeAudioConnection();
        channel.sendMessage("Disconnected from your channel").queue();

    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Makes the bot leave your channel";
    }
}

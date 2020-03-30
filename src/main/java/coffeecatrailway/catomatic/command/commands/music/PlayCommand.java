package coffeecatrailway.catomatic.command.commands.music;

import coffeecatrailway.catomatic.Config;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import coffeecatrailway.catomatic.music.PlayerManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class PlayCommand implements ICommand {

    private final YouTube youTube;

    public PlayCommand() {
        YouTube tmp = null;
        try {
            tmp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("CatoMatic Discord Bot").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.youTube = tmp;
    }

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            channel.sendMessage("Please provide some arguments").queue();
            return;
        }

        AudioManager audioManager = ctx.getGuild().getAudioManager();
        if (!audioManager.isConnected())
            JoinCommand.join(ctx);

        String input = String.join(" ", args);
        if (!isUrl(input)) {
            String ytSearched = searchYouTube(input);
            if (ytSearched == null) {
                channel.sendMessage("No results found on YouTube").queue();
                return;
            }
            input = ytSearched;
        }

        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(ctx.getChannel(), input);
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    @Nullable
    private String searchYouTube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("youtubekey"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();
                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a song in a voice channel\n"
                + "Usage: `" + getPrefixedName() + " <song url>`";
    }
}

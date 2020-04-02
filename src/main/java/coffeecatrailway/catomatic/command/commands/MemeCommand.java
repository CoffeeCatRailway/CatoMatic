package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author CoffeeCatRailway
 * Created: 2/04/2020
 */
public class MemeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> {
            JsonNode data = json.get("data");
            String url = data.get("image").asText();
            MessageEmbed embed = EmbedUtils.embedImage(url)
                    .setTitle(data.get("title").asText(), data.get("url").asText())
                    .build();
            ctx.getChannel().sendMessage(embed).queue();
        });
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp() {
        return "Shows a random me me";
    }
}

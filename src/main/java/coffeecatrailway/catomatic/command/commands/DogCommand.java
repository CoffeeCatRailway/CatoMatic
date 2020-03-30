package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Arrays;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class DogCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        WebUtils.ins.getJSONObject("https://random.dog/woof.json").async(json -> {
            String url = json.get("url").asText();
            MessageEmbed embed = EmbedUtils.embedImage(url).setTitle("Doggo").build();
            ctx.getChannel().sendMessage(embed).queue();
        });
    }

    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public String getHelp() {
        return "Shows a random dog";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("getdog", "randomdog");
    }
}

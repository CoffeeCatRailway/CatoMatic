package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
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
public class CatCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        WebUtils.ins.scrapeWebPage("https://api.thecatapi.com/api/images/get?format=xml&results_per_page=1").async(doc -> {
            String url = doc.getElementsByTag("url").first().html();
            MessageEmbed embed = EmbedUtils.embedImage(url).setTitle("Catto").build();
            ctx.getChannel().sendMessage(embed).queue();
            CommandManager.LOGGER.info("Loading cat...");
        });
    }

    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getHelp() {
        return "Shows a random cat";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("getcat", "randomcat");
    }
}

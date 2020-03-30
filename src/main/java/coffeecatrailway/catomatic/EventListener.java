package coffeecatrailway.catomatic;

import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class EventListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        Presence presence = event.getJDA().getPresence();
        presence.setActivity(Activity.listening(Config.get("prefix") + "help"));
        presence.setStatus(OnlineStatus.ONLINE);
        LOGGER.info("Logged in as {}", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage()) return;

        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id"))) {
            LOGGER.info("Shutting down!");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        }

        if (raw.startsWith(prefix))
            manager.handle(event);
    }
}

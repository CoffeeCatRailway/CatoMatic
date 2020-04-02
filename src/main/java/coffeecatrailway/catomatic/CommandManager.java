package coffeecatrailway.catomatic;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import coffeecatrailway.catomatic.command.commands.*;
import coffeecatrailway.catomatic.command.commands.admin.*;
import coffeecatrailway.catomatic.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class CommandManager {

    public static final String PREFIX = ":c";

    public static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {
        addCommand(new HelpCommand(this));
        addCommand(new AvatarCommand());
        addCommand(new PingCommand());
        addCommand(new CatCommand());
        addCommand(new DogCommand());
        addCommand(new DiceCommand());
        addCommand(new MemeCommand());

        addCommand(new KickCommand());
        addCommand(new BanCommand());
        addCommand(new UnbanCommand());
        addCommand(new SysPingCommand());
        addCommand(new UserInfoCommand());
        addCommand(new ServerInfoCommand());
        addCommand(new PurgeCommand());

        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
    }

    public void addCommand(ICommand command) {
        boolean nameFound = this.commands.keySet().stream().anyMatch(id -> id.equalsIgnoreCase(command.getName()));
        if (nameFound)
            throw new IllegalArgumentException("A command with this name is already present!");

        this.commands.put(command.getName(), command);
    }

    public Map<String, ICommand> getCommands() {
        return this.commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (Map.Entry<String, ICommand> entry : this.commands.entrySet())
            if (entry.getKey().equals(searchLower) || entry.getValue().getAliases().contains(searchLower))
                return entry.getValue();
        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(CommandManager.PREFIX), "")
                .split("\\s+");
        String invoke = split[0].toLowerCase();

        ICommand cmd = this.getCommand(invoke);
        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);
            cmd.handle(ctx);
        }
    }
}

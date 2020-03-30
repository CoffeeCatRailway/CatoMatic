package coffeecatrailway.catomatic;

import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import coffeecatrailway.catomatic.command.commands.*;
import coffeecatrailway.catomatic.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class CommandManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new SysPingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new AvatarCommand());
        addCommand(new PingCommand());
        addCommand(new CatCommand());
        addCommand(new DogCommand());

        addCommand(new KickCommand());

        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
    }

    public void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch(command -> command.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFound)
            throw new IllegalArgumentException("A command with this name is already present!");

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : commands)
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower))
                return cmd;
        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
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

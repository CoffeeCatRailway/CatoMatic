package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
import coffeecatrailway.catomatic.Config;
import coffeecatrailway.catomatic.command.CommandContext;
import coffeecatrailway.catomatic.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author CoffeeCatRailway
 * Created: 31/03/2020
 */
public class DiceCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        int sides = 6;
        int dices = 1;
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();

        if (!args.isEmpty()) {
            sides = Integer.parseInt(args.get(0));

            if (args.size() > 1)
                dices = Integer.parseInt(args.get(1));
        }

        if (sides > Integer.parseInt(Config.get("maxdicesides"))) {
            channel.sendMessage("The maximum sides is " + Config.get("maxdicesides")).queue();
            return;
        }
        if (dices > Integer.parseInt(Config.get("maxdices"))) {
            channel.sendMessage("The maximum dices is " + Config.get("maxdices")).queue();
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder().append("Results:\n");

        for (int d = 0; d < dices; d++)
            builder.append("\uD83C\uDFB2 #").append(d + 1).append(": **").append(random.nextInt(1, sides)).append("**\n");
        channel.sendMessage(builder.toString()).queue();
        CommandManager.LOGGER.info("rolling " + dices + "" + sides + " sided dice");
    }

    @Override
    public String getName() {
        return "dice";
    }

    @Override
    public String getHelp() {
        return "Rolls a dice\n"
                + "Usage: `" + getPrefixedName() + " [sides] [dices]`";
    }
}

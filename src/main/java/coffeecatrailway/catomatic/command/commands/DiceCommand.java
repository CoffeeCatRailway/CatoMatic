package coffeecatrailway.catomatic.command.commands;

import coffeecatrailway.catomatic.CommandManager;
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

    private final int maxSides = 100, minSides = 4, defaultSides = 6;
    private final int maxDice = 20, minDice = 1;

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();

        int sides = defaultSides;
        int dices = minDice;
        if (!args.isEmpty()) {
            sides = Integer.parseInt(args.get(0));
            if (args.size() > 1) dices = Integer.parseInt(args.get(1));
        }

        if (sides > maxSides) {
            channel.sendMessage("The maximum sides is " + maxSides).queue();
            return;
        }
        if (sides < minSides) {
            channel.sendMessage("The minimum sides is " + minSides).queue();
            return;
        }

        if (dices > maxDice) {
            channel.sendMessage("The maximum dice is " + maxDice).queue();
            return;
        }
        if (sides < minDice) {
            channel.sendMessage("The minimum dice is " + minDice).queue();
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

package coffeecatrailway.catomatic.command;

import coffeecatrailway.catomatic.Config;

import java.util.Arrays;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public interface ICommand {

    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    default List<String> getAliases() {
        return Arrays.asList();
    }

    default String getPrefixedName() {
        return Config.get("prefix") + getName();
    }
}

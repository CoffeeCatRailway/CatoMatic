package coffeecatrailway.catomatic.command;

import coffeecatrailway.catomatic.CommandManager;

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

    default HelpCategory getCategory() {
        return HelpCategory.NORMAL;
    }

    default List<String> getAliases() {
        return Arrays.asList();
    }

    default String getPrefixedName() {
        return CommandManager.PREFIX + getName();
    }

    enum HelpCategory {
        NORMAL("Normal"),
        MUSIC("Music"),
        ADMIN("Admin");

        String name;

        HelpCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

package coffeecatrailway.catomatic;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
}

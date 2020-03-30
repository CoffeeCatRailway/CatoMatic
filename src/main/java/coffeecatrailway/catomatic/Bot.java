package coffeecatrailway.catomatic;

import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * @author CoffeeCatRailway
 * Created: 30/03/2020
 */
public class Bot {

    private Bot() throws LoginException {
        new JDABuilder()
                .setToken(Config.get("token"))
                .addEventListeners(new EventListener())
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}

package net.verplanmich.bot.game.gaia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.verplanmich.bot.game.GameData;
import net.verplanmich.bot.game.GameResultException;

@SuppressWarnings("unchecked")
public class GaiaTest {

    @Test
    void getUsers() throws Exception {
        Gaia gaia = new Gaia();
        gaia.join(newUser("1", "Bob"), "ambas");
        gaia.join(newUser("2", "Sally"), "baltak");
        gaia.join(newUser("3", "Claudia"), "bescod");

        List<UserEntity> users = (List<UserEntity>) gaia.getUsers(new GameData()).getMap().get("users");

        assertThat(users).hasSize(3);
    }

    @Test
    void roundOrder() throws Exception {
        Gaia gaia = new Gaia();

        gaia.join(newUser("1", "Bob"), "ambas");
        gaia.join(newUser("3", "Sally"), "baltak");
        gaia.join(newUser("2", "Claudia"), "bescod");

        gaia.startRound("1");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("1");

        gaia.endTurn("1");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("3");

        gaia.endTurn("3");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("2");

        gaia.pass("2");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("1");

        gaia.pass("1");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("3");

        gaia.endTurn("3");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("3");

        assertThatThrownBy(() -> gaia.pass("3")).isInstanceOf(GameResultException.class);

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("2");
    }

    @Test
    void nextRoundOrder() throws Exception {
        Gaia gaia = new Gaia();

        gaia.join(newUser("1", "Bob"), "ambas");
        gaia.join(newUser("2", "Sally"), "baltak");
        gaia.join(newUser("3", "Claudia"), "bescod");
        gaia.startRound("1");

        gaia.pass("2");
        gaia.pass("3");
        try {
            gaia.pass("1");
        } catch (GameResultException e) {

        }

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("2");

        gaia.endTurn("2");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("3");

        gaia.endTurn("3");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("1");

        gaia.endTurn("1");

        assertThat((String) gaia.getCurrentUser().get("currentUser")).isEqualTo("2");
    }

    private GameData newUser(String userId, String userName) {
        GameData gameData = new GameData();
        gameData.setGameId("gaia");
        gameData.setUserId(userId);
        gameData.setUserName(userName);

        return gameData;
    }
}

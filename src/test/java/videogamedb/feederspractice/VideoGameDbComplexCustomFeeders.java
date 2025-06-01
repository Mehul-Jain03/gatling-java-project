package videogamedb.feederspractice;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * This class has examples of gatling complex custom feeders for post call.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbComplexCustomFeeders extends Simulation {

    private static Iterator<Map<String, Object>> customFeeder = Stream.generate((Supplier<Map<String, Object>>) () ->
            {
                Random rand = new Random();
                int gameId = rand.nextInt(10 - 1 + 1) + 1;
                String gameName = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
                String releaseDate = randomDate().toString();
                int reviewScore = rand.nextInt(100) + 1;
                String category = RandomStringUtils.randomAlphanumeric(5) + "-category";
                String rating = RandomStringUtils.randomAlphanumeric(4) + "-rating";
                HashMap<String, Object> hmap = new HashMap<>();
                hmap.put("gameId", gameId);
                hmap.put("gameName", gameName);
                hmap.put("releaseDate", releaseDate);
                hmap.put("reviewScore", reviewScore);
                hmap.put("category", category);
                hmap.put("rating", rating);
                return hmap;
            }
    ).iterator();
    private static ChainBuilder authenticate = exec(http("Get Auth Token").post("/authenticate")
            .body(StringBody("{\n" +
                    "  \"password\": \"admin\",\n" +
                    "  \"username\": \"admin\"\n" +
                    "}")).check(jmesPath("token").saveAs("token")));

    private static ChainBuilder createNewGame = feed(customFeeder).exec(http("Create New Game with - #{gameName}")
                    .post("/videogame")
                    .header("Authorization", "Bearer " + "#{token}")
                    .body(ElFileBody("bodies/newGameTemplate.json")).asJson().check(bodyString().saveAs("responseBody")))
            .exec(session -> {
                        System.out.println(session.getString("responseBody"));
                        return session;
                    }
            );
    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json").contentTypeHeader("application/json");
    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB")
            .exec(authenticate)
            .repeat(10).on(exec(createNewGame).pause(1));

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1)).protocols(httpProtocolBuilder));
    }

    public static LocalDate randomDate() {
        int hundredYears = 100 * 365;
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));
    }


}
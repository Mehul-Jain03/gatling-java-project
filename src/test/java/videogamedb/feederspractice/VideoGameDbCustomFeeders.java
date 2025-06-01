package videogamedb.feederspractice;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * This class has examples of gatling custom feeders for post call.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbCustomFeeders extends Simulation {

    private static Iterator<Map<String, Object>> customFeeder = Stream.generate((Supplier<Map<String, Object>>) () ->
            {
                Random rand = new Random();
                int gameId = rand.nextInt(10 - 1 + 1) + 1;
                return Collections.singletonMap("gameId", gameId);
            }
    ).iterator();

    private static ChainBuilder getVideoGameById = feed(customFeeder).exec(http("Get Video Game By - #{gameId}").get("/videogame/#{gameId}"));

    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json").contentTypeHeader("application/json");


    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB").exec(getVideoGameById);

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(50)).protocols(httpProtocolBuilder));
    }

}

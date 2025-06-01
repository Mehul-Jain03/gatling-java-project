package videogamedb.feederspractice;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class has examples of gatling csv feeders for post call.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbCSVFeeders extends Simulation {

    private static FeederBuilder.FileBased<String> csvFeeder = csv("data/gameCsvFile.csv").circular();

    private static ChainBuilder getVideoGameById = feed(csvFeeder).exec(http("Get Video Game By - #{gameId}").get("/videogame/#{gameId}").check(jmesPath("name").isEL("#{gameName}")));

    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json").contentTypeHeader("application/json");


    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB").exec(getVideoGameById);

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(50)).protocols(httpProtocolBuilder));
    }

}

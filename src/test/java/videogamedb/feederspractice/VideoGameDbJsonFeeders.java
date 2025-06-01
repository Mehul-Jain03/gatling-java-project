package videogamedb.feederspractice;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class has examples of gatling json feeders for post call.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbJsonFeeders extends Simulation {

    private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json").circular();

    private static ChainBuilder getVideoGameById = feed(jsonFeeder).exec(http("Get Video Game By - #{id}").get("/videogame/#{id}").check(jmesPath("name").isEL("#{name}")));

    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json").contentTypeHeader("application/json");

    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB").exec(getVideoGameById);

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(11)).protocols(httpProtocolBuilder));
    }
}

package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class has examples api chaining and looping.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbChainBuilder extends Simulation {

    private static ChainBuilder getAllVideoGames = exec(http("Get All Video Games").
            get("/videogame").check(status().is(200)).check(jmesPath("[0].id").saveAs("gameId")));

    private static ChainBuilder getVideoGameBasedOnId = repeat(3).on(exec(http("Get Video Game of Id - #{gameId}").
            get("/videogame/#{gameId}").check(status().is(200)).check(jmesPath("name").is("Resident Evil 4")).check(bodyString().saveAs("responseBody"))));

    private static ChainBuilder getVideoGameBasedOnCounter = repeat(5,"counterId").on(exec(http("Get Video Game of Id - #{counterId}").
            get("/videogame/#{counterId}").check(status().is(200))));


    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json");

    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB - Variables").
            exec(getAllVideoGames).
            pause(2).
            exec(getVideoGameBasedOnId).
            pause(2).
            exec(getVideoGameBasedOnCounter);

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1)).protocols(httpProtocolBuilder));
    }

}

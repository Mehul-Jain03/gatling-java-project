package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


/**
 * This class has examples of gatling add pause, check http status code and json path/jmes path assertion.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbAddPause extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder = http.
            baseUrl("https://www.videogamedb.uk/api").
            header("accept", "application/json");

    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB - Add Pause Scenario").
            exec(http("Get Game with id 1").get("/videogame/1").check(status().is(200)).check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4"))).pause(2).
            exec(http("Get All Games").get("/videogame/").check(status().in(200, 201, 204)).check(jmesPath("[?id==`2`].name").ofList().is(List.of("Gran Turismo 3")))).pause(2, 10).
            exec(http("Get Game with id 3").get("/videogame/3").check(status().not(400), status().not(500)).check(jsonPath("$[?(@.id==3)].name").is("Tetris"))).pause(Duration.ofMillis(3000));

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1)).protocols(httpProtocolBuilder));
    }

}
package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class shows basic gatling script.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class MyFirstTest extends Simulation {

    //setup api call, http configuration
    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json");

    //setup scenario definition
    private ScenarioBuilder scenarioBuilder = scenario("My First Test").exec(http("Get All Games").get("/videogame")).exec(http("Get Single Game").get("/videogame/1"));

    //setup load simulation
    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1)).protocols(httpProtocolBuilder));
    }


}
package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class has examples of Post call with auth and looping.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbPostCallWithAuthentication extends Simulation {

    private static ChainBuilder authenticate = exec(http("Get Auth Token").post("/api/authenticate")
            .body(StringBody("{\n" +
                    "  \"password\": \"admin\",\n" +
                    "  \"username\": \"admin\"\n" +
                    "}")).check(jmesPath("token").saveAs("token")));

    private static ChainBuilder createNewGame = exec(http("Create New Video Game").post("/api/videogame")
            .header("Authorization", "Bearer " + "#{token}")
            .body(StringBody("{\n" +
                    "  \"category\": \"Platform\",\n" +
                    "  \"name\": \"Mario\",\n" +
                    "  \"rating\": \"Mature\",\n" +
                    "  \"releaseDate\": \"2012-05-04\",\n" +
                    "  \"reviewScore\": 85\n" +
                    "}")).check(status().in(200, 201)));

    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk").
            header("accept", "application/json").
            contentTypeHeader("application/json");

    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB").
            exec(authenticate).
            pause(2).
            exec(createNewGame);

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1)).protocols(httpProtocolBuilder));
    }

}

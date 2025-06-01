package videogamedb.scriptfundamentals;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class has examples of correlation. session api. session api is used to print the particular session data or variable. If log level in log file is test then there is no need to print session data.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbVariable extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json");

    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB - Variables").
            exec(http("Get All Video Games").get("/videogame").check(status().is(200)).check(jmesPath("[0].id").saveAs("gameId"))).
            exec(session -> {
                System.out.println(session.getString("gameId"));
                return session;
            }).
            exec(http("Get Video Game of Id - #{gameId}").get("/videogame/#{gameId}").check(status().is(200)).check(jmesPath("name").is("Resident Evil 4")).check(bodyString().saveAs("responseBody"))).
            exec(session -> {
                System.out.println("Response is :" + session.getString("responseBody"));
                return session;
            });

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1)).protocols(httpProtocolBuilder));
    }

}

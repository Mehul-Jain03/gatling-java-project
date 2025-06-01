package videogamedb.simulationpractice;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class has examples of gatling simulations.
 *
 * @author Mehul Jain
 * @version 1.0
 * @since 2025-05-29
 */

public class VideoGameDbSimulation extends Simulation {

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "1"));
    private static final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION", "20"));

    @Override
    public void before() {
        System.out.printf("Running test with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
        System.out.printf("Total test duration: %d seconds%n", TEST_DURATION);
    }

    private static ChainBuilder getAllVideoGames = exec(http("Get All Video Games").
            get("/videogame").check(status().is(200)));

    private static ChainBuilder getVideoGameBasedOnId = exec(http("Get Video Game of Id - 2").
            get("/videogame/2").check(status().is(200)));

    private HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://www.videogamedb.uk/api").header("accept", "application/json");

    private ScenarioBuilder scenarioBuilder = scenario("VideoGame DB - Variables").
            exec(getAllVideoGames).
            pause(2).
            exec(getVideoGameBasedOnId);

    {
        setUp(scenarioBuilder.injectOpen(
                rampUsers(USER_COUNT).during(Duration.ofSeconds(RAMP_DURATION)),        // Warm-up phase
                rampUsers(USER_COUNT).during(Duration.ofSeconds(RAMP_DURATION)),        // Ramp-up to 100 users
                constantUsersPerSec(USER_COUNT).during(Duration.ofSeconds(RAMP_DURATION)), // Steady load
                rampUsers(1).during(Duration.ofSeconds(4))).       // Stress test up to 500
                protocols(httpProtocolBuilder)).maxDuration(TEST_DURATION);
    }

}
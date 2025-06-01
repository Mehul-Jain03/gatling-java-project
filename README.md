# About the Project
This project is structured to align basic gatling scripts for api's from https://www.videogamedb.uk/swagger-ui/index.html
Crafting performance test scenarios using Gatling's Java DSL.
Simulating various user behaviors and load patterns.
Analyzing performance metrics through Gatling's detailed reports.

# 🛠️ Technologies Used
Java 17+
Gatling 3.14 (Java DSL)
Maven for build automation
Git for version control

# Clone the Repository
git clone https://github.com/Mehul-Jain03/gatling-java-project.git
cd gatling-java-project

# Run a Simulation
mvn gatling:test -Dgatling.simulationClass=videogamedb.packageName.className -DUSERS=10 -DRAMP_DURATION=10 -DTEST_DURATION=10
Gatling will generate a detailed HTML report located in the target/gatling directory. Open the index.html file within the specific simulation folder to view the results.

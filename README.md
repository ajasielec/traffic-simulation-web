# 🚦 Intersection Simulation

## Overview
This project simulates traffic at an intersection. The simulation can be run via the command line or accessed through a web interface.

## Running the Simulation
### Command Line Execution
To start the simulation with specified input and output JSON files, use the following command:

```
mvn exec:java "-Dexec.args=input.json output.json"
```
Ensure that the JSON files are located in the src/main/resources folder.

If no arguments are provided, the program will run a random simulation, generating 10 commands by default:
```
mvn exec:java
```

### Web interface
You can run the backend using the following command:
```
mvn spring-boot:run
```
Once the backend is running, start the simulation by visiting:
http://localhost:8080

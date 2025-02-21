# 🚦 Intersection Simulation

## Overview
This project simulates traffic at an intersection. The simulation can be run via the command line or accessed through a web interface.

## Installation & Setup
First, clone the repository by running the following command in your terminal:
```
git clone https://github.com/ajasielec/traffic-simulation-web.git
```
Navigate to the project directory and install dependencies using Maven:
```
mvn clean install
```

## Running the simulation
### Command Line Execution
To start the simulation with specified input and output JSON files, run:
```
mvn exec:java "-Dexec.args=input.json output.json"
```
**Note: Ensure that the JSON input file is located in the *src/main/resources* folder.**

If no arguments are provided, the program will generate a random simulation with number of commands specified by user:
```
mvn exec:java
```
While running, the simulation prints status updates to the console with a delay. 
Results are saved in the specified output JSON file or in randomOutput.json in the *src/main/resources*.
Additionally, a randomInput.json file will be created with the randomly generated commands.


To run tests, use:
```
mvn test
```

### Web interface
To launch the backend server, run:
```
mvn spring-boot:run
```
Once the backend is running, access the simulation at:
http://localhost:8080 or http://localhost:63342


## How It Works
The simulation reads a JSON file containing commands (either provided by the user or randomly generated) and executes them step by step.

### Commands
There are two types of commands: 
* **addVehicle** - Adds a vehicle to the intersection on specified road.
* **step** - Advances the simulation by allowing vehicles to pass through the intersection to the end road. Vehicles from the most occupied roads are processed first and added to the leftVehicles list.

### Simulation Process

1. The simulation initializes an intersection instance and loads commands from the input file.
2. When executing an addVehicle command:
   *  The system adds a vehicle to the specified starting road at intersection (vehicles are placed in queues).
3. When executing a step:
   * The system checks if the traffic lights should switch, prioritizing the roads with more vehicles.
   * Traffic flows according to the active green light, removing vehicles from queues and adding them to the leftVehicles list.
   * The step results are saved to the step status list.
4. After executing every command, simulation results are saved to the specified output file.
5. The program also supports real-time updates via WebSockets, sending the current simulation state (traffic lights, added vehicles, etc.) to the frontend.

### Web Interface Functionality

When running the backend, you can start the simulation via the web interface:
* Enter input and output file names or choose a random simulation with a specified number of commands.
* Click the "Start Simulation" button to trigger the simulation via an API request.
* The results and status updates are displayed in real time in the simulation output window.


## Project structure
### application
* **Main** - The entry point of the application. It loads input/output file names, generates random input if needed, and starts the simulation.
* **Simulation** - Manages the simulation process. It loads commands from a file, executes them step by step, and saves results. It also supports real-time messaging via WebSockets.
### enums
* **Direction** - Represents the four cardinal directions (NORTH, EAST, SOUTH, WEST).
* **LightColor** - Defines traffic light states (GREEN, YELLOW, RED).
* **TrafficCycle** - Specifies which direction has the right of way (NORTH_SOUTH, EAST_WEST).
### models
* **Intersection** - Manages vehicle queues, controls traffic light switching, and simulates traffic flow.
* **TrafficLight** - Represents a traffic light with a color and cycle, handling transitions between states.
* **Vehicle** - Represents a vehicle with a unique ID, an entry direction, and an exit direction.
### dto
* **Command** - Represents a traffic command with type, vehicle ID, start, and end roads.
* **CommandList** - Singleton managing a list of commands, supports JSON loading.
* **StepStatus** - Tracks vehicles that left the intersection in a step.
* **SimulationResult** - Stores step-by-step simulation results
### services
* **RandomCommandGenerator** - Creates random "addVehicle" (with ID, start/end road) or "step" commands.
* **MessageSender** -  Interface for sending messages.
* **AbstractMessageSender** - Sends messages via SimpMessagingTemplate or prints them.
### utils
* **JsonUtils** - Handles JSON serialization/deserialization for CommandList and SimulationResult using ObjectMapper.
* **RandomJsonGenerator** - Generates a JSON file with random commands, using RandomCommandGenerator and saving via ObjectMapper.
### controllers
* **SimulationController (REST API)** - Starts a simulation or a random JSON-based simulation. Sends status updates via WebSocket.
* **WebSocketController** - Handles WebSocket messages and broadcasts updates to /topic/status.
### config
* **CorsConfig** - Enables CORS, allowing common HTTP methods.
* **WebSocketConfig** - Configures WebSocket with STOMP, /topic broker, /app prefix, and /ws endpoint (SockJS supported).
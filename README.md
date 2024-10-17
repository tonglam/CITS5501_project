# Domolect 2.0 REPL

This project implements a parser for the Domolect 2.0 command language, a system designed for controlling smart home
devices and appliances. It was developed as part of the CITS5501 unit extension task.

## Overview

The parser takes Domolect 2.0 commands as input, parses them, and simulates their execution.

The system supports two main types of commands:

1. **Basic Commands**: These include operations for various device types:
    - Lighting commands (e.g., turn lamp on/off)
    - Appliance commands (e.g., turn coffee-maker on/off)
    - Barrier commands (e.g., open/close gate, lock/unlock door)
    - Thermal device commands (e.g., set thermostat to specific temperature)

2. **Conditional Commands**: A key feature of Domolect 2.0 is the ability to specify conditions for command execution:
    - **Time-based conditions**: Commands can be set to execute at specific times.
      Example: `turn lamp on at 7:00 am`
    - **Temperature-based conditions**: Commands can be triggered based on temperature thresholds.
      Example: `turn air-conditioner on when current-temperature greater-than 30C`
    - **Compound conditions**: Commands can combine both time and temperature conditions.
      Example: `open windows when current-temperature greater-than 25C until 10:00 pm`

## Running the REPL using Docker

To run the parser using Docker, follow these steps:

1. Ensure you have Docker installed on your system.

2. Pull the Docker image:
   ```
   docker pull tonglam/cits5501-project:lastest
   ```

3. Run the parser using the following command:
   ```
   docker run -it tonglam/cits5501-project:lastest
   ```

## Running Example

Here's an example of running the REPL:

```shell
MacBook-Pro CITS5501_project % docker run -it --rm tonglam/cits5501-project:latest
Welcome to the Domolect 2.0 REPL. Type 'exit' to quit.
>>> turn bulb off when current-temperature greater-than 300 K
Command recognized: LightingCommand
When condition: TemperatureCondition at 300K, comparison: GREATER_THAN
Simulated execution: Turning off the bulb
```

## Project Structure

- `REPL.java`: The main class implementing the parsing and simulation functionality. (Note: Despite the name, this is
  not a full REPL implementation, but a parser that simulates command execution)
- `REPLTest.java`: A comprehensive suite of unit tests for the REPL class.

## Testing Approach

The primary testing approach adopted for this project is Data-Driven Testing. This method was chosen to thoroughly test
the parser's ability to handle a wide variety of Domolect 2.0 commands efficiently. The key aspects of our testing
approach include:

1. **Data-Driven Testing**: I used JUnit 5's `@ParameterizedTest` annotation to implement DDT. This allowed us to run
   the same test logic against multiple sets of input data, effectively covering a wide range of command variations
   without duplicating test code.

2. **Comprehensive Test Cases**: The test suite covers various aspects of the Domolect 2.0 language, including:
    - Basic commands (lighting, appliance, barrier, thermal device)
    - Commands with location specifications
    - Conditional commands (time-based and temperature-based)
    - Complex commands with multiple conditions
    - Edge cases and error conditions

3. **Boundary Value Analysis**: We included tests for boundary conditions, particularly for temperature values and time
   specifications.

4. **Error Handling**: Tests were designed to verify the parser's response to invalid inputs, ensuring robust error
   handling.

## Running the Tests

You can run the tests using your preferred IDE or build tool that
supports JUnit 5.

## Source Code Repository

The source code repository for this project is available
at: [https://github.com/tonglam/CITS5501_project.git](https://github.com/tonglam/CITS5501_project.git).

The Docker repository for this project is available at: [tonglam/cits5501-project](https://hub.docker.com/repository/docker/tonglam/cits5501-project/general).

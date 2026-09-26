# SpringBot

SpringBot is a Spring Boot-based keyboard automation service for solving grid-based games and automating keyboard input.

Currently supported:

* **Zip** — record arrow-key movements, reverse them, and replay them
* **Sudoku** — enter a Sudoku solution using keyboard automation
* **Tango** — enter `S` / `M` patterns using SPACE presses
* **N-Queen** — place queens on an `N × N` board using coordinate positions

---

## Requirements

* Java 21+
* Maven
* macOS
* Accessibility / Input Monitoring permission for the application running Java
* A graphical Java environment because the project uses `java.awt.Robot`

### macOS Java Robot

Run the application with headless mode disabled:

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.awt.headless=false -Dapple.awt.UIElement=true"
```

If running the packaged JAR:

```bash
java -Djava.awt.headless=false -Dapple.awt.UIElement=true -jar target/SpringBot-0.0.1-SNAPSHOT.jar
```

---

# 1. Start SpringBot

From the project root:

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash

mkdir -p logs


java \
  -Djava.awt.headless=false \
  -Dapple.awt.UIElement=true \
  -Dloader.path=release/lib \
  -jar release/SpringBot-1.0.0.jar \
  > logs/springbot-console.log 2>&1

```

Run it in background

```bash
mkdir -p logs

nohup java \
  -Djava.awt.headless=false \
  -Dapple.awt.UIElement=true \
  -Dloader.path=release/lib \
  -jar release/SpringBot-1.0.0.jar \
  > logs/springbot-console.log 2>&1 &
```


You'll immediately get the PID:
```sh
echo $!
```
Follow the log

```sh
tail -f logs/springbot-console.log
```

Stop it:
```sh
pkill -f 'SpringBot-1.0.0.jar'
```

The application runs on:

```text
http://localhost:8080
```

---

# 2. Check Service Health

Before using any automation, verify that SpringBot is running:

```bash
curl -X GET \
"http://localhost:8080/api/health"
```

Expected result should indicate that the service is up.

---

# 3. Zip Solver

The Zip solver can:

1. Start global arrow-key monitoring
2. Record `UP`, `DOWN`, `LEFT`, and `RIGHT`
3. Stop monitoring
4. Reverse the recorded instruction if required
5. Execute the keyboard sequence

## Step 1 — Start Monitoring

```bash
curl -X POST \
"http://localhost:8080/api/zip/command/track/start"
```

After starting monitoring, switch to the Zip game and solve/record the arrow movements.

Only the supported arrow keys are recorded.

---

## Step 2 — Record Arrow Movement

Play the Zip game normally while SpringBot is monitoring.

The recorded sequence may look like:

```text
UP,UP,RIGHT,DOWN,LEFT,LEFT,UP
```

---

## Step 3 — Stop Monitoring

```bash
curl -X POST \
"http://localhost:8080/api/zip/command/track/stop"
```

The API returns the recorded arrow commands.

---

## Step 4 — Reverse the Instruction

If the recorded solution was solved in the opposite direction, reverse the instruction.

Example:

```bash
curl -X GET \
"http://localhost:8080/api/zip/command/reverse?instruction=LEFT"
```

For a complete sequence:

```bash
curl -X GET \
"http://localhost:8080/api/zip/command/reverse?instruction=UP,UP,RIGHT,DOWN,LEFT"
```

The reverse operation reverses both:

* The order of commands
* The direction of each command

For example:

```text
UP,RIGHT,DOWN
```

becomes:

```text
UP,LEFT,DOWN
```

---

## Step 5 — Execute Zip Instructions

Execute an instruction using:

```bash
curl -X POST \
"http://localhost:8080/api/zip/command?instruction=UP"
```

Multiple commands can be supplied:

```bash
curl -X POST \
"http://localhost:8080/api/zip/command?instruction=UP,UP,RIGHT,RIGHT,DOWN,LEFT"
```

The automation will switch to the previous application and execute the keyboard sequence.

---

# 4. Sudoku Solver

The Sudoku automation accepts a flattened Sudoku solution.

For example:

```text
634251152364426513513642361425245136
```

Execute it with:

```bash
curl -X POST \
"http://localhost:8080/api/sudoku/execute?instruction=634251152364426513513642361425245136"
```

The service:

1. Parses the digits
2. Determines the Sudoku grid size
3. Converts the values into a grid
4. Traverses the grid in zigzag order
5. Types the corresponding numbers
6. Moves to the next cell

For a standard `6 × 6` Sudoku, the instruction contains:

```text
36 digits
```

---

# 5. Tango Solver

Tango uses:

```text
S = press SPACE once
M = press SPACE twice
```

**The numbers `1` and `2` are never typed.**

For example:

```text
S M S M
```

generates:

```text
SPACE
SPACE SPACE
SPACE
SPACE SPACE
```

The board is traversed in zigzag order:

```text
Row 0:  LEFT → RIGHT
Row 1:  RIGHT → LEFT
Row 2:  LEFT → RIGHT
Row 3:  RIGHT → LEFT
...
```

Execute a Tango solution with:

```bash
curl -X POST \
"http://localhost:8080/api/tango/execute?instruction=MSSMMSSMMSSMMSMMSSSMSMMSMSMSSMSMSSMM"
```

The instruction must contain exactly `N × N` values for the Tango board.

---

# 6. N-Queen Solver

The N-Queen automation places `N` queens on an `N × N` board.

Queen positions are provided using:

```text
row,column
```

with zero-based indexing.

For example:

```text
[0,8]
[1,1]
[2,4]
[3,7]
[4,0]
[5,3]
[6,6]
[7,2]
[8,5]
```

can be supplied as:

```text
0,8;1,1;2,4;3,7;4,0;5,3;6,6;7,2;8,5
```

## Execute N-Queen

For a `9 × 9` board:

```bash
curl -X POST \
"http://localhost:8080/api/nqueen/execute?n=9&positions=0,8;1,1;2,4;3,7;4,0;5,3;6,6;7,2;8,5"
```

### Traversal

The automation starts at:

```text
[0,0]
```

and traverses the board in a zigzag pattern.

```text
Row 0:  LEFT → RIGHT
Row 1:  RIGHT → LEFT
Row 2:  LEFT → RIGHT
Row 3:  RIGHT → LEFT
...
```

When the cursor reaches a queen position:

```text
SPACE
SPACE
```

is pressed to place the queen.

No number keys are pressed.

For example:

```text
[0,8]
```

means:

```text
Move to row 0, column 8
SPACE
SPACE
```

Then the cursor moves down to the next row.

---

# 7. N-Queen Position Format

The recommended format is:

```text
row,column;row,column;row,column
```

Example:

```text
0,8;1,1;2,4;3,7;4,0;5,3;6,6;7,2;8,5
```

Coordinates are **zero-based**.

For a `9 × 9` board:

```text
Minimum row = 0
Maximum row = 8

Minimum column = 0
Maximum column = 8
```

Exactly `9` queen positions must be supplied.

---

# 8. API Summary

| Feature                 | Method | Endpoint                       |
| ----------------------- | ------ | ------------------------------ |
| Health                  | GET    | `/api/health`                  |
| Start Zip monitoring    | POST   | `/api/zip/command/track/start` |
| Stop Zip monitoring     | POST   | `/api/zip/command/track/stop`  |
| Reverse Zip instruction | GET    | `/api/zip/command/reverse`     |
| Execute Zip             | POST   | `/api/zip/command`             |
| Execute Sudoku          | POST   | `/api/sudoku/execute`          |
| Execute Tango           | POST   | `/api/tango/execute`           |
| Execute N-Queen         | POST   | `/api/nqueen/execute`          |

---

# 9. Quick Start

Start the application:

```bash
mvn spring-boot:run
```

Check health:

```bash
curl -X GET \
"http://localhost:8080/api/health"
```

Then choose the required solver.

### Zip

```bash
curl -X POST \
"http://localhost:8080/api/zip/command?instruction=UP,UP,RIGHT,DOWN"
```

### Sudoku

```bash
curl -X POST \
"http://localhost:8080/api/sudoku/execute?instruction=634251152364426513513642361425245136"
```

### Tango

```bash
curl -X POST \
"http://localhost:8080/api/tango/execute?instruction=MSSMMSSMMSSMMSMMSSSMSMMSMSMSSMSMSSMM"
```

### N-Queen

```bash
curl -X POST \
"http://localhost:8080/api/nqueen/execute?n=9&positions=0,8;1,1;2,4;3,7;4,0;5,3;6,6;7,2;8,5"
```

---

# 10. Important Notes

### macOS Permissions

Because SpringBot uses `java.awt.Robot` and global keyboard monitoring, macOS may require permissions under:

```text
System Settings
→ Privacy & Security
→ Accessibility
```

and, for global keyboard monitoring:

```text
System Settings
→ Privacy & Security
→ Input Monitoring
```

Grant the required permissions to the application/process actually running SpringBot, such as Terminal, VS Code, or the Java runtime.

### Application Switching

The automation can switch to the previous application using:

```text
⌘ + Tab
```

This is controlled by:

```properties
automation.keyboard.switch-application=true
```

To disable automatic application switching:

```properties
automation.keyboard.switch-application=false
```

### Keyboard Speed

SpringBot uses:

```java
robot.setAutoDelay(0);
robot.setAutoWaitForIdle(false);
```

to minimize keyboard automation latency.

The automation therefore executes keyboard events as quickly as the target application can accept them.

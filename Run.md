```sh
mvn spring-boot:run

mvn spring-boot:run -DskipTests


mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.awt.headless=false"


```

Package as jar : `mvn clean package -DskipTests`

Skip compiling/running tests completely `mvn spring-boot:run -Dmaven.test.skip=true`


Useful Spring Boot combinations
Purpose	Command
Normal development	mvn spring-boot:run
Run, skip tests	mvn spring-boot:run -DskipTests
Clean + run	mvn clean spring-boot:run
Clean + run, skip tests	mvn clean spring-boot:run -DskipTests
Build JAR	mvn clean package
Build JAR, skip tests	mvn clean package -DskipTests
Build JAR, completely skip tests	mvn clean package -Dmaven.test.skip=true
Run JAR	java -jar target/*.jar
Install locally	mvn clean install -DskipTests


mvn clean install -DskipTests



---


Run spring boot application

For your exact workflow, I'd run it from a background terminal process, not as a Java GUI application.

For example:

```sh
mvn clean package -DskipTests
```


then:


```sh
nohup java \
  -Djava.awt.headless=false \
  -jar target/SpringBot-0.0.1-SNAPSHOT.jar \
  > logs/springbot-console.log 2>&1 &
```

Immediately after starting, you'll get something like:
[1] 48291

48291 is the PID.

Stop it gracefully: `kill 48291`

Check: `ps -p 48291`

If it doesn't stop after a few seconds, force it: `kill -9 48291`


Even easier for your development workflow

You can store the PID automatically:

```sh
nohup java \
  -Djava.awt.headless=false \
  -jar target/SpringBot-0.0.1-SNAPSHOT.jar \
  > logs/springbot-console.log 2>&1 &

echo $! > springbot.pid
```


Then stop it later with:
```sh
kill $(cat springbot.pid)
```


And remove the PID file:
```sh
rm springbot.pid
```

See whether SpringBot is running

Because your API is on port 8080:

```sh
lsof -i :8080
```

Or:
```sh
curl http://localhost:8080/api/health
```
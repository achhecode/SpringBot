```sh
mvn spring-boot:run

mvn spring-boot:run -DskipTests


mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.awt.headless=false"

```

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
FROM gradle:8.5.0-jdk21 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
RUN gradle build --no-daemon || return 0
COPY src ./src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

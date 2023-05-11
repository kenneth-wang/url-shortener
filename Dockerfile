FROM amazoncorretto:17-alpine-jdk

# create a directory for the app
WORKDIR /app

COPY . .

# Build the app
RUN ./gradlew clean build -x test

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/demo-0.0.1-SNAPSHOT.jar"]



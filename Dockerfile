FROM openjdk:17
WORKDIR /app
COPY build/libs/test-0.0.1-SNAPSHOT.jar test.jar
CMD ["java", "-jar", "test.jar"]
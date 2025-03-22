FROM openjdk:17
WORKDIR /app

COPY . .
RUN ./gradlew build -x test

COPY build/libs/seoulmate-0.0.1-SNAPSHOT.jar seoulmate.jar
CMD ["java", "-jar", "seoulmate.jar"]
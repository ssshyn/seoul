FROM openjdk:17
WORKDIR /app
COPY build/libs/seoulmate-0.0.1-SNAPSHOT.jar seoulmate.jar
CMD ["java", "-jar", "seoulmate.jar"]
FROM openjdk:17
WORKDIR /app
COPY build/libs/seoulmate-0.0.1-SNAPSHOT.jar seoulmate.jar
ENV USE_PROFILE=local
CMD ["java", "-jar", "seoulmate.jar"]
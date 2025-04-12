FROM openjdk:17
WORKDIR /app
COPY build/libs/seoulmate-0.0.1-SNAPSHOT.jar seoulmate.jar
ENV SPRING_PROFILES_ACTIVE=dev
ENV GOOGLE_CLIENT_SECRET=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
CMD ["java", "-jar", "seoulmate.jar"]
FROM openjdk:17
WORKDIR /app

COPY . .
RUN ./gradlew clean -x test && ./gradlew build -x test

# 빌드 후, build/libs/ 디렉토리 내용 출력하여 JAR 파일이 생성되었는지 확인
RUN ls -alh build/libs/

COPY build/libs/seoulmate-0.0.1-SNAPSHOT.jar seoulmate.jar
CMD ["java", "-jar", "seoulmate.jar"]
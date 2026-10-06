FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY maven-settings.xml /root/.m2/settings.xml

COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
ENV TZ=Asia/Shanghai

COPY --from=build /app/target/kaifangyuanzi-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

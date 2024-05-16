FROM openjdk:21-jdk

WORKDIR /app

COPY ./target/mobiauto-backend-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "mobiauto-backend-0.0.1-SNAPSHOT.jar"]

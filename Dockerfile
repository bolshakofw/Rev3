FROM openjdk:17
ADD /target/Demo-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar","backend.jar"]
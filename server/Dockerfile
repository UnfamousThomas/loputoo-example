FROM openjdk:21-jdk

WORKDIR server

COPY build/libs/loputoo-example-usage-1.0.0.jar /server/server.jar

EXPOSE 25565
CMD ["java", "-jar", "server.jar"]
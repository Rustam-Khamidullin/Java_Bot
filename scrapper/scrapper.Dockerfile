FROM openjdk:21
WORKDIR /app
COPY ./target /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/scrapper.jar"]

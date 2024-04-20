FROM openjdk:21
WORKDIR /app
COPY ./target /app
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]

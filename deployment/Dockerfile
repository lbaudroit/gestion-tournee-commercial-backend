#Ne lancer que dans githubAction
FROM eclipse-temurin:23
RUN addgroup --system spring && adduser --system --ingroup spring springUser
USER springUser
WORKDIR /gestion-tournee-commercial-backend
ARG JAR_FILE=backendtourneecommercial.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
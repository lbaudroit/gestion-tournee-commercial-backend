#Ne lancer que dans githubAction
FROM eclipse-temurin:23.0.2_7-jdk-noble
RUN addgroup --system spring && adduser --system --ingroup spring springUser
USER springUser
WORKDIR /gestion-tournee-commercial-backend
EXPOSE 9090
ARG JAR_FILE=backendtourneecommercial.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/gestion-tournee-commercial-backend/app.jar"]

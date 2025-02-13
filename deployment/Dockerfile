FROM eclipse-temurin:23
RUN apt-get update && apt-get install -y --no-install-recommends \
    adduser \
    && rm -rf /var/lib/apt/lists/*
RUN addgroup --system spring && adduser --system --ingroup spring springUser
USER springUser
WORKDIR /gestion-tournee-commercial-backend
RUN echo "$(pwd)"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
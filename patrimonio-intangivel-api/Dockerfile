FROM registry.nexus.azi.srv.br/hal/images/base-java11:latest

ENV APP_JAR=patrimonio-intangivel-api.jar

COPY patrimonio-intangivel-api-application/pom.xml .

RUN get-jarfile ./pom.xml $APP_JAR

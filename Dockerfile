# premier conteneur télécharge les dépendances du projet avec maven

# https://fr.wikipedia.org/wiki/OpenJ9
FROM maven:3.8.1-jdk-11-openj9 AS MAVEN_TOOL_CHAIN

COPY pom.xml /tmp/
RUN mvn -B dependency:go-offline -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml
COPY src /tmp/src/
# fabrique le répertoire tmp s'il n'existe pas
# puis rentrer dans le répertoire tmp
WORKDIR /tmp/
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package


# second conteneur 
# - va récupérer les dépendances téléchargées par le précédent
# - va ensuite copier notre application Spring et la lancer 
FROM openjdk:11.0.11-jdk-oracle

EXPOSE 8081

RUN mkdir /app
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar /app/spring-boot-application.jar
# "-Dspring.profiles.active=test"
CMD [ "-Djava.security.egd=file:/dev/./urandom", "-Dmaven.test.skip=true" ]
# j'ai mis spring en profile de "test" via le paramètre Dspring.profiles
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]
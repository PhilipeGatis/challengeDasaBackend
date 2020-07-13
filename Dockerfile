FROM openjdk:8-jdk-alpine
ADD target/labor-manager*.jar labor-manager.jar
RUN sh -c 'touch /labor-manager.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/labor-manager.jar"]

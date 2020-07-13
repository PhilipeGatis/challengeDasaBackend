FROM openjdk:8
COPY ./target/labor-manager-*.jar ./labor-manager.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/labor-manager.jar"]

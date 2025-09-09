#FROM amazoncorretto:21
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
#ENV CATSGRAM_IMAGE_DIRECTORY = "images"



FROM amazoncorretto:21
WORKDIR /app
COPY target/shareit-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "shareit-0.0.1-SNAPSHOT.jar"]

FROM java:8
ADD ./target/vulnmanager-1.0-SNAPSHOT.jar //
ENTRYPOINT ["java", "-jar", "/vulnmanager-1.0-SNAPSHOT.jar"]

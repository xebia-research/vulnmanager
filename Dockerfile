FROM ubuntu
FROM java:8

RUN apt-get update
RUN apt-get install maven git default-jre -y
VOLUME /volume/git
RUN mkdir -p /local/git
WORKDIR /local/git/
CMD ["/bin/bash"]
RUN git clone https://github.com/xebia-research/vulnmanager
WORKDIR /local/git/vulnmanager
RUN mvn package -DskipTests=true
RUN java- jar /local/git/vulnmanager/target/vulnmanager-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/local/git/vulnmanager/target/vulnmanager-1.0-SNAPSHOT.jar"]


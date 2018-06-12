FROM ubuntu:16.04
RUN apt-get update
RUN apt-get install apt-utils -y
RUN apt-get install maven git apt-utils -y
VOLUME /volume/git
RUN mkdir -p /local/git
WORKDIR /local/git/
RUN git clone -b docker-maven https://github.com/xebia-research/vulnmanager
WORKDIR /local/git/vulnmanager
RUN apt-get install default-jdk -y
RUN mvn install -DskipTests=true
RUN mvn package -DskipTests=true

FROM java:8
RUN mkdir -p /opt/
COPY --from=0 /local/git/vulnmanager/target/vulnmanager-1.0-SNAPSHOT.jar /opt/vulnmanager-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/opt/vulnmanager-1.0-SNAPSHOT.jar"]

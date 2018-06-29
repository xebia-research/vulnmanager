#stage1
FROM ubuntu:16.04 as builder
RUN apt-get update
RUN apt-get install maven git default-jdk -y
VOLUME /volume/git
RUN mkdir -p /local/git
WORKDIR /local/git/
RUN git clone -b feature/universal-branch-docker-support https://github.com/xebia-research/vulnmanager
WORKDIR /local/git/vulnmanager
RUN git pull
RUN git branch

RUN bash ./scripts/genSec.sh
RUN bash ./dockerScripts/dbDefinition.sh

RUN mvn install -DskipTests=true
RUN mvn package -DskipTests=true

#stage 2
FROM java:8 as runner
RUN mkdir -p /opt/
COPY --from=builder /local/git/vulnmanager/target/vulnmanager-1.0-SNAPSHOT.jar /opt/vulnmanager-1.0-SNAPSHOT.jar
RUN mkdir -p /opt/exmaple_logs
COPY --from=builder /local/git/vulnmanager/example_logs /example_logs
ENTRYPOINT ["java", "-jar", "/opt/vulnmanager-1.0-SNAPSHOT.jar"]

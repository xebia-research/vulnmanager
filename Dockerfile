FROM ubuntu:16.04
FROM java

RUN apt-get update
RUN apt-get install maven git default-jre apt-utils -y
VOLUME /volume/git
RUN mkdir -p /local/git
WORKDIR /local/git/
RUN git clone https://github.com/xebia-research/vulnmanager
WORKDIR /local/git/vulnmanager
RUN mvn package -DskipTests=true
RUN java -jar /local/git/vulnmanager/target/vulnmanager-1.0-SNAPSHOT.jar


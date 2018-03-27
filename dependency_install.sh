#!/bin/bash
clear
# check and install docker
printf "===========Checking if docker-ce is already installed===========\n"
if docker -v 2>/dev/null; then
	printf '\n===========Docker is already installed!===========\n'
else
	printf '\n===========Docker is not yet installed, will do that now.===========\n'
	sudo apt-get install \
	apt-transport-https \
	ca-certificates \
	curl \
	software-properties-common -y
	curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
	sudo add-apt-repository \
	"deb [arch=amd64] https://download.docker.com/linux/ubuntu \
	$(lsb_release -cs) \
	stable"
	sudo apt update
	sudo apt-get install docker-ce -y
	if docker -v 2>/dev/null; then
		printf "\n===========Docker was installed successfully!===========\n"
		sleep 2
	else printf "\n===========Docker failed to Launch, it's likely installation failed===========\n"
	sleep 2
	fi
fi

# check and install docker-compose 
printf "\n===========Checking if docker-compose is already installed===========\n"
if docker-compose -v 2>/dev/null; then 
	printf "===========Docker-compose is already installed!===========\n\n"
else
	printf "\n===========Docker-compose is not yet installed, will do that now.===========\n"
	sleep 1
	sudo curl -L https://github.com/docker/compose/releases/download/1.20.1/$
	sudo chmod +x /usr/local/bin/docker-compose
	if docker-compose -v 2>/dev/null; then
		printf "\n===========Docker-compose installed succesfully!===========\n"
		sleep 2
	else printf "\n===========Docker-compose failed to launch, it's likely installation failed===========\n"
	sleep 2
	fi
fi

# check and install maven
printf "===========Checking if maven is installed..===========\n"
if mvn -v 2>/dev/null; then
	printf "===========Maven is already installed!============\n"
else
	printf "\n===========Maven is not yet installed, will do that now.===========\n"
	sudo apt install maven -y
	if mvn -v 2>/dev/null; then
		printf "===========Maven installed successfully!===========\n"
	else printf "\n===========Maven install failed, it's likely installation failed.===========\n"
	fi
fi

if docker -v && docker-compose -v && mvn -v; then
	clear
	printf "\n===========All dependencies installed successfully!===========\n"
else printf "\n\n===========Not all dependencies were installed successfully :(===========\n===========Scroll up to find out what went wrong..===========\n"
fi

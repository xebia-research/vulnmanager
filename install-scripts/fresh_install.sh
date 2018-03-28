#!/bin/bash
clear
sudo apt update && sudo apt upgrade -y && sudo apt dist-upgrade -y
sudo add-apt-repository ppa:openjdk-r/ppa -y
sudo apt-get update
sudo apt-get install openjdk-8-jdk -y
sudo apt install curl -y
sudo ./dependency_install.sh
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs

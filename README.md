# vulnmanager

[![Build Status](https://travis-ci.org/xebia-research/vulnmanager.svg?branch=develop)](https://travis-ci.org/xebia-research/vulnmanager)

## This is a vulnerability manager with an authenticated API to manage vulnerability reports.

__We currently plan to support the following programs:__ 
 * [x] Nmap #16 / #20
 * [x] OpenVAS (14 / #18)
 * OWASP ZAP (#13 / #17)
 * Clair (#15 / #19)

Ultimately, This will have a WebGUI to manage everything, and will be written in Angular. (#23)

## Dependencies:
 * Docker
 * Maven (This will download the rest of the dependencies with __mvn install__)

Everything is run in docker, for your convenience ! :whale:

1. Make sure the dependencies are installed 
2. Git Clone this repo
2. Run "install_docker.sh" to automagically build, pull and compose the necessary docker containers. 

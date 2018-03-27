# vulnmanager

[![Build Status](https://travis-ci.org/xebia-research/vulnmanager.svg?branch=develop)](https://travis-ci.org/xebia-research/vulnmanager)

## This is a vulnerability manager with an authenticated API to manage vulnerability reports.

__We currently plan to support the following programs:__ 
 * [x] Nmap [#16](https://github.com/xebia-research/vulnmanager/issues/16) / [#16](https://github.com/xebia-research/vulnmanager/issues/20)
 * [x] OpenVAS [#14](https://github.com/xebia-research/vulnmanager/issues/14) / [#18](https://github.com/xebia-research/vulnmanager/issues/18)
 * OWASP ZAP [#13](https://github.com/xebia-research/vulnmanager/issues/13) / [#17](https://github.com/xebia-research/vulnmanager/issues/17)
 * Clair [#15](https://github.com/xebia-research/vulnmanager/issues/15) / [#19](https://github.com/xebia-research/vulnmanager/issues/19)

Ultimately, This will have a WebGUI to manage everything, and will be written in Angular. [#23](https://github.com/xebia-research/vulnmanager/issues/23)

## Dependencies:
 * Docker
 * Maven (This will download the rest of the dependencies with __mvn install__)

Everything is run in docker, for your convenience ! :whale:

1. Make sure the dependencies are installed 
2. Git Clone this repo
2. Run "install_docker.sh" to automagically build, pull and compose the necessary docker containers. 

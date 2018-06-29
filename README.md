# Vulnmanager

[![Build Status](https://travis-ci.org/xebia-research/vulnmanager.svg?branch=develop)](https://travis-ci.org/xebia-research/vulnmanager)

#### This is a vulnerability manager with an authenticated API to manage vulnerability reports in a webGUI.
##### Be sure to check our [Wiki](https://github.com/xebia-research/vulnmanager/wiki) for up-to-date instructions and info!
__We currently support the following scanning tools:__ 
 * Nmap
 * OpenVAS
 * OWASP ZAP
 * Clair
 
#### Report formats 
The vulnmanager parser currently supports the following report formats per scan tool:

 | Scanning Tool | Report Format | 
 | ------------- | ------------- |
 | Clair | JSON |
 | OpenVAS | XML |
 | ZAP | XML & JSON |
 | Nmap | XML |


## Dependencies:
 #### Production
 * Docker

#### Development
 * Npm 5+
 * nodeJS 8+
 * Postgresql 10
 * Maven (Maven can download the rest of the dependencies with __mvn install__)
 * AngularCLI
 
## Production installation:
Everything is run in docker, for your convenience ! :whale:
During `docker-compose build`, a JWT secret is generated 
1. git clone this repo
2. _cd_ into this repo
3. run '__docker-compose build__' and wait(!) for it to finish
4. run '__docker-compose up -d__'

## Usage

- After running docker-compose, you can reach the GUI at __localhost:4242__

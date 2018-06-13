### STAGE 0: git clone
FROM alpine as cloner
RUN mkdir -p /git/
WORKDIR /git/

RUN apk --update add git openssh && \
    rm -rf /var/lib/apt/lists/* && \
    rm /var/cache/apk/*

RUN git clone -b docker-maven https://github.com/xebia-research/vulnmanager.git
RUN ls -lah
RUN pwd
## STAGE 1: Build

FROM node:8.11-alpine as node-angular-cli

RUN apk update \
  && apk add --update alpine-sdk \
  && apk del alpine-sdk \
  && rm -rf /tmp/* /var/cache/apk/* *.tar.gz ~/.npm \
  && npm cache verify \
  && sed -i -e "s/bin\/ash/bin\/sh/" /etc/passwd
#Angular CLI
RUN npm install -g @angular/cli
RUN mkdir -p /git/
COPY --from=cloner /git/vulnmanager/ /git/
WORKDIR /git/vulnmanager/GUI/
RUN pwd && ls -lah
RUN npm install
RUN npm i npm@latest -g
RUN ng build --prod
RUN pwd && ls -lah
### STAGE 1: Setup ###

FROM nginx:1.13.3-alpine as webserver

## Copy our default nginx config
COPY nginx/default.conf /etc/nginx/conf.d/

## Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

## From 'builder' stage copy over the artifacts in dist folder to default nginx public folder
COPY --from=builder /git/vulnmanager/GUI/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]

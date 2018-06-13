### STAGE 0: git clone
FROM alpine as cloner
RUN mkdir -p /git/
WORKDIR /git/

RUN apk --update add git openssh && \
    rm -rf /var/lib/apt/lists/* && \
    rm /var/cache/apk/*

RUN git clone -b docker-maven https://github.com/xebia-research/vulnmanager.git


# We label our stage as ‘builder’
FROM node:8.1.4-alpine as builder

COPY --from=cloner /git/vulnmanager/GUI ./

## Storing node modules on a separate layer will prevent unnecessary npm installs at each build
RUN npm i && mkdir /ng-app && cp -R ./node_modules ./ng-app

WORKDIR /ng-app

COPY . .

## Build the angular app in production mode and store the artifacts in dist folder
RUN npm i -g @angular/cli
RUN npm install rxjs@6 rxjs-compat@6
RUN ng build --prod


### STAGE 2: Setup ###

FROM nginx:1.13.3-alpine

## Copy our default nginx config
COPY nginx/default.conf /etc/nginx/conf.d/

## Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

## From ‘builder’ stage copy over the artifacts in dist folder to default nginx public folder
COPY --from=builder /ng-app/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]



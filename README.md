metagrapher
===========

A tool to cartograph services via eureka metadata

![Actual Screenshot](https://raw.githubusercontent.com/comsysto/metagrapher/orphans/resources/metagrapher-screenshot-1.png "Actual Screenshot")

Building docker images
----------------------

    mvn clean package fabric8:build

Trying the example application stack
------------------------------------

    docker-compose -f docker-compose.yaml up

Opening [metagrapher ui](http://localhost:8080/metagrapher_ng)

Opening [eureka ui](http://localhost:8080/eureka)



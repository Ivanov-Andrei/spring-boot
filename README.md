# spring-boot
test application for making CRUD operation to the DB with spring-boot, spring data
# requirements
- you need at least java version 8
- maven version 3.6.2
- postgesql server version 10.5
# how to start application in a docker container
For running application in a docker container you need to execute next commands:
```
docker build -t <container name> .
```
after that run it:
```
docker run -i --net=<network name> --rm -p 8080:8080 <container name> 
```
# how to run postgresql server in docker container
```
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name <name> -e POSTGRES_USER=<user> -e POSTGRES_PASSWORD=<pass> -e POSTGRES_DB=<db name> -p 5432:5432 postgres:10.5 --net=<network name>
```
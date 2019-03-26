# Commit Viewer
A Java application that provides REST APIs for retrieving commits of the specified git hub repository.

**Author**: Amy Lin

## Getting Started
The app consists of three parts - MongoDB (`http://localhost:27017`) and a Java web service (`http://localhost:8080`).
The Local runtime environment can be created with docker compose. 

1. Clone the repository
2. Build the server
```
./gradlew build
```
3. In the root directory, run `docker-compose up -d --build` to create the Docker images
4. Verify the containers are running and the ports are exposed with `docker ps`
5. Post to the API endpoints with the following payload 

```
{
    "projectOwner": "project owner",
    "projectName": "project name"
}

```
to get commits 
* Using git API`http://localhost:8080/commits-remote`, or
* Cloning the project into a temp folder and use git CLI `http://localhost:8080/commits`

To retest cloning when calling `/commits`, use DELETE `/projects` endpoint to wipe the database and recall `/commits`.
To check the app log, run `docker logs commit-viewer-backend-server`. To check the database log, run 
`docker logs commit-viewer-mongodb`.

## Assumptions
* The git hub project allows cloning via https.
* The first time the user calls `/commits`, it will clone the remote project and get a list of commits using git CLI and
store it in the database. From then on whenever the user calls `/commits` it will return whatever is stored in the database. 
* The MongoDB container is not configured to retain any data when it is destroyed. This can be done via volume mount 
in the docker-compose file should it be necessary.

## Future enhancements
* Install 
* Allow the user to pass in a local project directory and execute get commits from that directory first before cloning.
* Implement the complete git commit object model so every detail of a commit can be stored. As a result, when the user 
passes in options of CLI to query specific information about the commits (e.g. `--pretty=oneline` or 
`--pretty=format:"%h - %an, %ar : %s"`), the data model will be able to accommodate it. 
* Host the solution in the cloud, e.g. AWS, and rewrite REST endpoints as lambda functions.
* Automatically update the list of commits when the list of commits returned by git CLI has new commits, with a scheduler.
* Enable user authentication so the app can also be used for private git repositories.
* Provide Swagger doc on API endpoints.

## Built With
Java Springboot backend
* [Spring boot](https://spring.io/projects/spring-boot) - The Java backend framework
* [Gradle](https://docs.gradle.org/current/userguide/userguide.html) - Java dependency management
* [Java](https://hub.docker.com/_/java) - Java 1.8 Docker image

MongoDB
* [MongoDB](https://hub.docker.com/_/mongo) - MongoDB Docker image

## Running the tests
### Java unit tests
In the `commit-viewer` directory, run
```
./gradlew test
```

### Java controller endpoint tests (with Postman)
#### End points testing with Postman

```
POST /commits-remote HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: 41d4ccce-899a-4e22-dcef-e238e2dbdf8c

{
	"projectOwner": "alin27",
	"projectName": "aws-cloud-formation-stack-manager"
}

```
Clone a project and get a list of commits using git CLI
```
POST /commits HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: 953576cd-7099-49f7-c87d-53410c0ab288

{
	"projectOwner": "alin27",
	"projectName": "aws-cloud-formation-stack-manager"
}
```
Delete all projects stored in the database
```
DELETE /projects HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache}

```
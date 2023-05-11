# A URL shortener web app

## Introduction
- This is a web app to input a URL and receive a shortened URL.
- Upon accessing the shortened URL with a browser, you will be redirected to your original URL.
- The web app consists of a React frontend and a Kotlin backend.
- A PostgreSQL relational database is used to save the URL records for persistence.

## Usage
1. Head to [the deployed website](http://ec2-13-213-0-245.ap-southeast-1.compute.amazonaws.com/).
2. Follow the instructions on the screen.
3. You will be redirected to a website to enter a URL that you wish to shorten. Enter a URL and click on the `"Shorten"` button.
4. You will see a shortened URL.
5. Click on the shortened URL and be redirected to the website you have given in `step #3`.

Here is an illustrated view of the whole process
![](demo.gif)

## Deployment to AWS
The web app is deployed on a public AWS EC2 server with Docker Compose. The deployment steps are documented as follows:

- Run the following commands to install Docker-compose/Docker/Git. Restart the server. 
    ```shell
    sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s | tr '[:upper:]' '[:lower:]')-$(uname -m) -o /usr/bin/docker-compose && sudo chmod 755 /usr/bin/docker-compose && docker-compose --version
    sudo yum install git -y
    sudo yum install docker containerd
    sudo systemctl enable docker.service --now
    sudo usermod -a -G docker ec2-user
    ```
- Clone the repository
    ```shell
        git clone https://github.com/kenneth-wang/url-shortener.git
    ```
- Create the following `application.yml` under `src/main/resources/`
  ```.yaml
        spring:
            profiles:
                active: mock
            datasource:
                url: jdbc:postgresql://db:5432/postgres
                password: postgres
                username: postgres
                schema: classpath:/sql/schema.sql
            jpa:
                hibernate:
                    ddl-auto: update
            properties:
                hibernate:
                    dialect: org.hibernate.dialect.PostgreSQLDialect
            baseBackendUrl: http://localhost:8080
        ---
        
        spring:
            profiles:
                active: database
            datasource:
                url: jdbc:postgresql://db:5432/postgres
                password: postgres
                username: postgres
                schema: classpath:/sql/schema.sql
            jpa:
                hibernate:
                    ddl-auto: update
            properties:
                hibernate:
                    dialect: org.hibernate.dialect.PostgreSQLDialect
            baseBackendUrl: <Your public server's DNS>
  ``` 
- Set these two fields to your public server's DNS:
  - `proxy` under `./frontend/package.json`
  - `spring.baseBackendUrl` under `src/main/resources/application.yml`
- Start the app by running the following commands
    ```sh
    cd url-shortener
    docker-compose up --build
    ```

## Tests
- Integration tests for the APIs can be found under:
  - `src/test/kotlin/com/example/demo/urls/controller`
- Unit tests for mock classes and service logic can be found under:
  - `src/test/kotlin/com/example/demo/urls/datasource/mock`
  - `src/test/kotlin/com/example/demo/urls/service`

## Other notes
- For simplicity:
  - The web app was deployed with Docker Compose on a single EC2 server. However, for actual production purposes, it would be more ideal to deploy the frontend/backend apps on different servers through a serverless option such as AWS ECS Fargate.
  - I did not purchase a domain
  - I have committed the code to `main` branch instead of creating a `dev`/`feature` branches. 

## Personal notes
### Steps to create a local React app
1. Create React app
    ```shell
    npx create-react-app frontend 
    ```

2. Install dependencies and start app
    ```shell
    cd frontend
    npm install react-router-dom
    npm install axios
    ```
   
3. For local development, set proxy in package.json
    ```
    proxy=http://localhost:8080
    ```
   
4. Start app
    ```shell
    npm start
    ```

### Steps to run adhoc code in IntelliJ
1. Go to `Tools > Kotlin > Kotlin REPL`
2. If any build errors appear, go to `Build > Rebuild Project`
3. Paste code into terminal

### References:
- [Spring Boot with Kotlin & JUnit 5](https://www.youtube.com/watch?v=TJcshrJOnsE)
- [Build a CRUD Rest API with Kotlin, Postgres, Docker and docker compose](https://www.youtube.com/watch?v=BbT1PCAOS2s)
- [FrancescoXX / kotlin-live Github Repo](https://github.com/FrancescoXX/kotlin-live/tree/main)
- [petersommerhoff / thenewboston-spring-boot-with-kotlin Gihut Repo](https://github.com/petersommerhoff/thenewboston-spring-boot-with-kotlin)
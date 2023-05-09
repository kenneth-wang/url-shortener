# An URL shortener web app

## Introduction
- This is a web app to input a URL and received a shortened URL. Upon accessing the shortened URL with a browser, you will be redirected to your original URL.
- The web app consists of a React frontend and a Kotlin backend.
- A PostgreSQL relational database is used to save the URL records for persistence.

## Usage
1. Head to XX/
2. Click on the embedded link
3. You will be redirected to a website to enter a URL that you wish to shorten. Click on the "Shorten" button.
    1. If you do not have a URL in mind, you may enter `XX` as the URl to shorten
4. You will see a shortened URL appear. Click on the shortened URL and be redirected to the website belonging to the input URL in step #3
    1. If `XX` is the URL that you have entered in Step #3, you will be redirected to `YY`

## Deployment
- The web app is deployed on a public AWS EC2 server with Docker Compose
- The following command was used to deploy the PostgreSQL relational database, React frontend and Kotlin backend
```sh
docker-compose up --build
```

## Unit Tests
These are the unit tests that have been implemented:
- ??


## Personal notes
- Create React app
```shell
npx create-react-app frontend 
```
- Install dependencies and start app
```shell
cd frontend
npm install react-router-dom
```
- Add proxy in package.json
```
proxy=http://localhost:8080
```
- Start app
```shell
npm start
```
# multi-tenant-starbucks

## Software dependency:

### Local environment:

Language: Python 2.7 or 3.5 (preferred)
Library: flask, requests

### Docker environment:

Software tool: Docker

## Backend dependency:

Before the front-end sever is run, in addition to satisfy software requirements, make sure the following steps are met:

1. Have at least 2 api backends running.

Golang backend:
        
        https://github.com/azhadm/multi-tenant-starbucks/blob/master/Khoa.Restbucks/README.md

Java backend:

        https://github.com/azhadm/multi-tenant-starbucks/blob/master/Azhad.Restbucks/README.md

1. Have a Kong sever running with appropriate routing setup for 2 uris, SanJose and SanFrancisco, the two current location in our fronend. See the links for more detail:

        https://github.com/azhadm/multi-tenant-starbucks/blob/master/Prachi.Kong/README.md

2. Update application variable application.config['KONG_BASE_URL'] to have the base DNS address of Kong sever.

3. Rename Dockerfile.local to Dockerfile if testing in Docker environment is desired

## Test/Run application:

### Run in local environment:

1. After installing Python language and required packages, go to directory where application.py is located and run the following command : 

        python application.py

2. Visit localhost:8080 to test application

### Run in Docker:

1. After installing Docker and renaming Dockerfile.local to Dockerfile, go to directory where Dockerfile is located and build an image.

######   syntax:
        docker build -t <image_name> .

######   eg:
        docker build -t starbucks_frontend .

2. To check if the image is built successfully, run the following command and look for your <image_name>.

        docker images

3. Run the docker image: 

######   syntax:
        docker run -it --rm -p 3000:8080 <image_name>

######   eg:
        docker build -it --rm -p 3000:8080 starbucks_frontend

4. Visit localhost:3000 to test application

## Deploy to AWS Elastic Beanstalk:

Comming soon
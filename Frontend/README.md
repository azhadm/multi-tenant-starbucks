# multi-tenant-starbucks

## Software dependency:

### Local environment:

Language: Python 2.7 or 3.5 (preferred)
Library: flask, requests

### Docker environment:

Software tool: Docker

## Backend dependency:

Before the front-end sever is run, in addition to satisfy software requirements, make sure the following steps are met:

1. Have a Kong sever running with appropriate routing setup for 2 uris, SanJose and SanFrancisco, the two current location in our fronend. See the links for more detail:

#####   syntax:
        https://github.com/azhadm/multi-tenant-starbucks/blob/master/Prachi.Kong/README.md

2. Update application variable application.config['KONG_BASE_URL'] to have the base DNS address of Kong sever.

3. Rename Dockerfile.local to Dockerfile if testing in Docker environment is desired

## Test/Run application:

### Run in local environment:

1. After installing Python language and required package, go to directory where application.py is located and run the following command : 

######  syntax:
        python application.py

2. Visit localhost:8080 to test application

# Proxy Server
A proxy server that only accepts CONNECT and whitelisted hosts defined in Constants.

# Requirements
This project requires JavaSE-11 so gradle wrapper could work properly.

# Build tool
Gradle wrapper is bundled with the source code.

# Usage
Unzip the project and go to the project root where gradlew is located and run following commands:
1. ./gradlew clean build
2. ./gradlew run

# Things to be improved
1. Logging.
2. Unit tests. Unit tests for server code need to be added. I haven't been able to find some time to write tests for the server code due to my upcoming releases.
3. Thread handling/pooling for tunneling.
4. Proxy other types of requests.
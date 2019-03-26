FROM openjdk:8-jre-slim
MAINTAINER Amy Lin <lin.amy8208@gmail.com>

RUN apt-get update && apt-get install -y git

ADD ./build/libs/commit-viewer-1.0.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/commit-viewer-1.0.jar"]
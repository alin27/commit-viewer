FROM java:8-jre
MAINTAINER Amy Lin <lin.amy8208@gmail.com>

ADD ./build/libs/lightning-talks-1.0.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/commit-viewer-1.0.jar"]
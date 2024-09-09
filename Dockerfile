FROM openjdk:17.0.2-oraclelinux8
LABEL maintainer="AntonyCheng"

ENV PARAMS=""

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /opt/security-operation

COPY target/security-operation-*.jar /opt/security-operation/app.jar

VOLUME ["/opt/security-operation"]

EXPOSE 38080

EXPOSE 38081

EXPOSE 38082

EXPOSE 39999

CMD ["sh","-c","java -jar $JAVA_OPTS /opt/security-operation/app.jar $PARAMS"]
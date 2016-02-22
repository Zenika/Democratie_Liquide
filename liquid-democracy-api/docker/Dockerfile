FROM java:8
VOLUME /tmp
WORKDIR /usr/bin/app/
COPY liquid-democracy-api.jar ./liquid-democracy-api.jar
RUN bash -c 'touch ./liquid-democracy-api.jar'
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","./liquid-democracy-api.jar"]
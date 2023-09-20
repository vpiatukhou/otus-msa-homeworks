#
# 1st step. Create a lightweight JRE.
#
FROM amazoncorretto:17.0.3-alpine as jre-creator
RUN apk add --no-cache binutils

RUN $JAVA_HOME/bin/jlink \
    --verbose \
    --add-modules java.base,java.desktop,java.instrument,java.management,java.naming,java.security.jgss,java.sql,jdk.httpserver \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /appjre

#
# 2nd step. Build the application.
#
FROM amazoncorretto:17.0.3-alpine as app-builder
RUN apk add --no-cache maven

#create a build directory
WORKDIR /usr/src/app/

#copy the sources
COPY . .

#build the application
RUN mvn clean package && \
    mv ./target/profile-service-*-with-dependencies.jar ./target/app.jar #rename the produced jar

#
# 3d stage. Create a runnable image.
#
FROM alpine:3.18.2

#set up the JRE
ENV JAVA_HOME=/opt/jre
ENV PATH=${JAVA_HOME}/bin:${PATH}
COPY --from=jre-creator /appjre $JAVA_HOME

ARG APP_USER=app
ARG APP_HOME=/opt/app/

RUN \
    #Add a new user. The application will be run on behalf of this user.
    addgroup -S $APP_USER && \
    adduser --no-create-home -S $APP_USER -G $APP_USER && \
    #create the installation directory
    mkdir $APP_HOME && \
    chown $APP_USER $APP_HOME && \
    #The new user can execute files in the directory. Other users have only read access.
    chmod 544 $APP_HOME
#switch to the new user.
USER $APP_USER

#install the application
WORKDIR $APP_HOME
COPY --from=app-builder /usr/src/app/target/app.jar .

CMD ["java", "-jar", "app.jar"]
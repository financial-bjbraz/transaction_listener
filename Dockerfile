FROM maven:3.8.1-openjdk-17-slim as builder
COPY ./pom.xml /tmp
COPY src/ /tmp/src/
WORKDIR /tmp
RUN mvn package
ARG aws_region
ARG aws_access_key_id
ARG aws_secret_access_key
ARG keycloadk_url
ARG mongodb_url
ARG redis_url
ARG logstash_url

ARG rabbit.queue.name
ARG spring.rabbitmq.host
ARG spring.rabbitmq.port
ARG spring.rabbitmq.username
ARG spring.rabbitmq.password
ARG web3j.client-address

ENV RABBIT_QUEUE_NAME=$rabbit.queue.name
ENV RABBIT_QUEUE_HOST=$spring.rabbitmq.host
ENV RABBIT_QUEUE_PORT=$spring.rabbitmq.port
ENV RABBIT_QUEUE_USERNAME=$spring.rabbitmq.username
ENV RABBIT_QUEUE_PASSWORD=$spring.rabbitmq.password

ENV AWS_REGION=$aws_region
ENV AWS_ACCESS_KEY_ID=$aws_access_key_id
ENV KEYCLOACK=$keycloadk_url
ENV MONGODB_URI=$mongodb_url
ENV REDIS=$redis_url
ENV LOGSTASH=$logstash_url
RUN mvn package -DskipTests
FROM openjdk:17
COPY --from=builder /tmp/target/transaction_listener-0.0.1-SNAPSHOT.jar /tmp/
COPY target/classes/logback-spring.xml /config/
#EXPOSE 8090

ENV JAVA_OPTS="-Xmx128m -Xms128m -XshowSettings:vm -XX:MetaspaceSize=48m -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport -Dlogging.config=file:/config/logback-spring.xml"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /tmp/transaction_listener-0.0.1-SNAPSHOT.jar" ]
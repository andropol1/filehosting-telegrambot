FROM maven:3.8-openjdk-17-slim

ENV HOME=/home/app
RUN mkdir -p $HOME

COPY ./settings.xml /usr/share/maven/ref
ENV MVN_SETTINGS=/usr/share/maven/ref/settings.xml

RUN mkdir -p $HOME/dispatcher
COPY ./dispatcher/pom.xml $HOME/dispatcher
COPY ./dispatcher/src $HOME/dispatcher/src
RUN mvn -f $HOME/dispatcher/pom.xml -s $MVN_SETTINGS clean package -DskipTests --debug

CMD java -jar $HOME/dispatcher/target/dispatcher-0.0.1.jar
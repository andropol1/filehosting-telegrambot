FROM maven:3.8-openjdk-17-slim

ENV HOME=/home/app
RUN mkdir -p $HOME

COPY ./settings.xml /usr/share/maven/ref
ENV MVN_SETTINGS=/usr/share/maven/ref/settings.xml

RUN mkdir -p $HOME/common
COPY ./common $HOME/common
RUN mvn -f $HOME/common/pom.xml -s $MVN_SETTINGS clean package
RUN mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=$HOME/common/target/common-0.0.1.jar --debug

RUN mkdir -p $HOME/rest-service
COPY ./rest-service/pom.xml $HOME/rest-service
COPY ./rest-service/src $HOME/rest-service/src
RUN mvn -f $HOME/rest-service/pom.xml -s $MVN_SETTINGS clean package -DskipTests --debug

CMD java -jar $HOME/rest-service/target/rest-service-0.0.1.jar
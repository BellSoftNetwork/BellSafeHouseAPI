FROM gradle:7.5.0-jdk17

ENV APP_HOME=/home/gradle/project

USER gradle

RUN mkdir $APP_HOME

WORKDIR $APP_HOME

CMD ["/bin/bash", "-c", "./scripts/docker/entrypoint.sh"]

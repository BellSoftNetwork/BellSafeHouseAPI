#!/bin/bash

cd "${APP_HOME}" || echo "'${APP_HOME}' 디렉토리를 찾을 수 없습니다." || exit 1

gradle --stop
gradle build --gradle-user-home .gradle-cache --continuous --quiet &
gradle --gradle-user-home .gradle-cache bootRun --stacktrace

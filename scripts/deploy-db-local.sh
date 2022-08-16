#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"
source "${BASH_SOURCE%/*}/libs/check-deploy-requirements.sh"

# EXCEPTION
if [ ! -d "${DATABASE_MYSQL_DATA_HOST_PATH}" ]; then
  ### 권한 획득
  echo "mysql 데이터 디렉토리 생성에 관리자 권한이 필요하여 1회성 권한을 요청합니다"
  if [ "$(sudo id -u)" != "0" ]; then
    echo "관리자 권한을 획득하지 못하여 설치를 중단합니다."

    exit 1
  fi

  sudo mkdir -p "${DATABASE_MYSQL_DATA_HOST_PATH}"
  sudo chmod 777 "${DATABASE_MYSQL_DATA_HOST_PATH}"
fi

# CONFIG
export DEPLOYMENT_YAML_NAME="${DATABASE_DEPLOYMENT_YAML_NAME}"
export APPLICATION_DEPLOYMENT_NAME="${DATABASE_MYSQL_SERVICE_NAME}"

source "${BASH_SOURCE%/*}/libs/deploy-k8s-resource.sh"

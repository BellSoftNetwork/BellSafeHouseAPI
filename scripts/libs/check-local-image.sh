#!/bin/bash

if [ "$(${DOCKER} image inspect --format="ok" "${APPLICATION_LOCAL_IMAGE_NAME}")" != "ok" ]; then
  echo "로컬 환경용 이미지가 없습니다. '${PROJECT_ROOT_PATH}/scripts/init-project.sh' 스크립트를 실행하세요."

  exit 1
fi

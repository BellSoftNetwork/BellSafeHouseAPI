#!/bin/bash

echo "로컬 이미지 빌드 시작 ({${APPLICATION_LOCAL_IMAGE_NAME}})"
${DOCKER} build "${PROJECT_ROOT_PATH}" -t "${APPLICATION_LOCAL_IMAGE_NAME}"

### 이미지 빌드 확인
if [ "$(${DOCKER} image inspect --format="ok" "${APPLICATION_LOCAL_IMAGE_NAME}")" != "ok" ]; then
  echo "도커 이미지가 정상적으로 빌드되지 않았습니다. 아래 명령을 수행할 권한이 있는지 확인 후 재시도 하시기 바랍니다."
  echo "도커 빌드 명령어: '${DOCKER} build ${PROJECT_ROOT_PATH} -t ${APPLICATION_LOCAL_IMAGE_NAME}'"

  exit 1
fi
echo "도커 이미지 생성 확인 완료"
echo

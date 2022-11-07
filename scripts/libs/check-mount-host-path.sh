#!/bin/bash

# CONFIG
BIND_PATH="${DOCKER_HOST_MOUNT_PATH_PREFIX}"

### 마운트 상태 확인
echo "마운트 상태 확인 시작 (findmnt ${BIND_PATH})"
MOUNT_COUNT=$(findmnt "${BIND_PATH}" | sed 1d | wc -l)

if [ "${MOUNT_COUNT}" != "1" ]; then
  echo "호스트 경로가 정상적으로 마운트되지 않았습니다. (${MOUNT_COUNT})"
  echo "공통 개발환경 설정 가이드 문서에 '/etc/fstab' 파일 설정 부분을 진행하세요."

  exit 1
fi

echo "정상 마운트 확인 완료"
echo

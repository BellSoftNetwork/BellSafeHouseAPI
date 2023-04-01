#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

# Get list of pods
POD_LIST=$(${KUBECTL} -n "${PROJECT_NAMESPACE}" get -o custom-columns=appType:.metadata.name pods | tail -n +2)

# Pod selection
if [[ -n $POD_LIST ]]; then
  echo "로그를 출력할 Pod를 선택하세요."
  select POD_NAME in $POD_LIST; do
    printf "\n"
    if [[ $POD_NAME = "" ]]; then
      echo "Pod를 찾을 수 없습니다. 네임스페이스 ${PROJECT_NAMESPACE}를 확인하시거나 쿠버네티스 상태를 확인하십시오."
      exit 1
    fi
    break
  done
else
  echo "Pod를 찾을 수 없습니다. 네임스페이스 ${PROJECT_NAMESPACE}를 확인하시거나 쿠버네티스 상태를 확인하십시오."
  exit 1
fi

# Get container list
CONTAINER_LIST_INIT=$(${KUBECTL} -n "${PROJECT_NAMESPACE}" get -o custom-columns=containerType:.spec.initContainers[*].name pods "$POD_NAME" | tail -n +2 | grep -v "<none>")
CONTAINER_LIST=$(${KUBECTL} -n "${PROJECT_NAMESPACE}" get -o custom-columns=containerType:.spec.containers[*].name pods "$POD_NAME" | tail -n +2)

# set CONTAINER_LIST_ALL to joined with init container, non-init container
# if not exists, just set to $CONTAINER_LIST
if [[ -n $CONTAINER_LIST_INIT ]]; then
  CONTAINER_LIST_ALL=$(echo "$CONTAINER_LIST_INIT,$CONTAINER_LIST")
else
  CONTAINER_LIST_ALL=$CONTAINER_LIST
fi

# Container selection
if [[ -n $CONTAINER_LIST_ALL ]]; then
  OLD_IFS=$IFS
  IFS=','
  echo "로그를 출력 할 컨테이너를 선택하세요."
  select container in $CONTAINER_LIST_ALL; do
    SELECTED_CONTAINER=$container
    IFS=$OLD_IFS
    printf "\n"
    break
  done
else
  echo "Pod를 찾을 수 없습니다. 네임스페이스 ${PROJECT_NAMESPACE}를 확인하시거나 쿠버네티스 상태를 확인하십시오."
  IFS=$OLD_IFS
  exit 1
fi

# Print range selection
echo "출력할 로그 라인 수를 입력하세요. (미입력시 follow 모드로 진입)"
read LINE_TO_PRINT
if [[ -n $LINE_TO_PRINT ]]; then
  ${KUBECTL} -n "${PROJECT_NAMESPACE}" logs "${POD_NAME}" -c "${SELECTED_CONTAINER}" | tail -n "$LINE_TO_PRINT"
  echo "로그 출력이 종료되었습니다."
  exit 0
else
  # Follow
  ${KUBECTL} -n "${PROJECT_NAMESPACE}" logs -f "${POD_NAME}" -c "${SELECTED_CONTAINER}"
  echo "로그 출력이 종료되었습니다."
  exit 0
fi

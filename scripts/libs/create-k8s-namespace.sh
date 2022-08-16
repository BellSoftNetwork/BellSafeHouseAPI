#!/bin/bash

# CONFIG
NAMESPACE_STATUS="$(${KUBECTL} get ns "${PROJECT_NAMESPACE}" -o=jsonpath='{.status.phase}')"

# PROCESS
if [ "${NAMESPACE_STATUS}" != "Active" ]; then
  ${KUBECTL} create ns "${PROJECT_NAMESPACE}"
  echo "k8s 네임스페이스 생성 완료"
else
  echo "k8s 네임스페이스가 이미 생성됨"
fi

#!/bin/bash

# CONFIG
NAMESPACE_STATUS="$(${KUBECTL} get ns "${PROJECT_NAMESPACE}" -o=jsonpath='{.status.phase}')"

# PROCESS
if [ "${NAMESPACE_STATUS}" != "Active" ]; then
  echo "k8s 네임스페이스가 존재하지 않습니다"
else
  ${KUBECTL} delete ns "${PROJECT_NAMESPACE}"
  echo "k8s 네임스페이스 제거 완료"
fi
echo

#!/bin/bash

# EXCEPTION
# TODO: 추후 필요 시 deployment 가 존재하는지 확인



# REDEPLOY KUBERNETES RESOURCES
echo "-- 프로젝트 Kubernetes 리소스 재시작 --"

${KUBECTL} -n "${PROJECT_NAMESPACE}" rollout restart deploy "${APPLICATION_DEPLOYMENT_NAME}"
echo "리소스 배포 재시작 요청 완료"

echo "리소스 배포 상태 확인"
if ! ${KUBECTL} -n "${PROJECT_NAMESPACE}" rollout status deployment "${APPLICATION_DEPLOYMENT_NAME}"; then
  echo "재시작 실패"

  exit 1
fi

echo "-- 프로젝트 Kubernetes 리소스 재시작 완료 --"

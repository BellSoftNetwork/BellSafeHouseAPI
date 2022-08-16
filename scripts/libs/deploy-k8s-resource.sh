#!/bin/bash

# CONFIG
DEPLOYMENT_YAML_PATH="${KUBERNETES_DEPLOY_YAML_PATH}/${DEPLOYMENT_YAML_NAME}"


# CHECK
echo "-- 생성된 Kubernetes 리소스 확인 --"
EXISTS_FILE_COUNT=$(find "${KUBERNETES_DEPLOY_YAML_PATH}" -name "${DEPLOYMENT_YAML_NAME}" -type f | wc -l)

if [ "${EXISTS_FILE_COUNT}" != "0" ]; then
  echo "이미 생성된 배포 리소스가 있습니다. 리소스 정의 템플릿이 달라졌을 경우 기존 리소스 제거가 필요합니다."
  echo "기존 k8s 리소스 제거 및 리소스 정의 파일을 삭제하시겠습니까? (1 or 2)"
  select yn in "Yes" "No"; do
    case $yn in
      Yes )
        ${KUBECTL} delete -f "${DEPLOYMENT_YAML_PATH}"
        echo "k8s 리소스 제거 완료"

        rm -v "${DEPLOYMENT_YAML_PATH}"
        echo "리소스 파일 삭제 완료"
        echo

        break;;
      No )
        echo "기존 리소스 정의 파일을 덮어씌웁니다. 리소스 정의 템플릿이 달라졌을 경우 제거되지 않은 리소스가 있을 수 있습니다."
        echo

        break;;
    esac
  done
fi



# CREATE KUBERNETES RESOURCES
${ENVSUBST} < "${KUBERNETES_RESOURCE_PATH}/${DEPLOYMENT_YAML_NAME}" > "${DEPLOYMENT_YAML_PATH}"
echo "Kubernetes 리소스 정의 파일 생성 완료"
echo



# DEPLOY KUBERNETES RESOURCES
echo "-- 프로젝트 Kubernetes 리소스 배포 시작 --"

${KUBECTL} apply -f "${DEPLOYMENT_YAML_PATH}"
echo "리소스 배포 설정 완료"

if [ "${APPLICATION_DEPLOYMENT_NAME}" != "" ]; then
  echo "리소스 배포 상태 확인"
  if ! ${KUBECTL} -n "${PROJECT_NAMESPACE}" rollout status deployment "${APPLICATION_DEPLOYMENT_NAME}"; then
    echo "Rollback to the previous version because the application deployment failed."
    ${KUBECTL} -n "${PROJECT_NAMESPACE}" rollout undo deployment "${APPLICATION_DEPLOYMENT_NAME}"

    if ! ${KUBECTL} -n "${PROJECT_NAMESPACE}" rollout status deployment "${APPLICATION_DEPLOYMENT_NAME}"; then
      echo "Application rollback is failed"
    else
      echo "Application rollback is complete"
    fi

    exit 1
  fi
fi

echo "-- 프로젝트 Kubernetes 리소스 배포 완료 --"

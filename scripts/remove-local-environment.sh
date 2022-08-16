#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"

# PROCESS
echo "-- 로컬 배포 환경 제거 시작 --"

## DELETE NAMESPACE
source "${BASH_SOURCE%/*}/libs/delete-k8s-namespace.sh"

## DELETE RESOURCE FILES
rm -v "${KUBERNETES_DEPLOY_YAML_PATH}"/*.yaml;
echo "리소스 파일 삭제 완료"
echo


echo "-- 로컬 배포 환경 제거 완료 --"

#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"
source "${BASH_SOURCE%/*}/libs/check-deploy-requirements.sh"

# PROCESS
echo "-- 프로젝트 환경 기본설정 시작 --"

## CHECK HOST PATH MOUNT
source "${BASH_SOURCE%/*}/libs/check-mount-host-path.sh"

## 로컬 이미지 빌드
source "${BASH_SOURCE%/*}/libs/build-local-image.sh"

## CREATE NAMESPACE
source "${BASH_SOURCE%/*}/libs/create-k8s-namespace.sh"

## 공통 리소스 생성
export DEPLOYMENT_YAML_NAME="${COMMON_DEPLOYMENT_YAML_NAME}"

source "${BASH_SOURCE%/*}/libs/deploy-k8s-resource.sh"

## 린트 설정
source "${BASH_SOURCE%/*}/libs/setup-ide.sh"

echo "-- 프로젝트 환경 기본설정 완료 --"

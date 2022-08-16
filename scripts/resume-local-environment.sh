#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"
source "${BASH_SOURCE%/*}/libs/check-deploy-requirements.sh"

# CONFIG
export DEPLOYMENT_YAML_PATH="${KUBERNETES_DEPLOY_YAML_PATH}/${VIEW_DEPLOYMENT_YAML_NAME}"
export APPLICATION_DEPLOYMENT_NAME="${VIEW_APPLICATION_DEPLOYMENT_NAME}"

export K8s_DEPLOY_REPLICAS="1"
export K8s_DEPLOY_NAMESPACE="${PROJECT_NAMESPACE}"

# Scale
echo "API 스케일 설정 시작"
export K8s_DEPLOY_NAME="${API_APPLICATION_DEPLOYMENT_NAME}"
source "${BASH_SOURCE%/*}/libs/scale-k8s-deploy.sh"

echo "View 스케일 설정 시작"
export K8s_DEPLOY_NAME="${VIEW_APPLICATION_DEPLOYMENT_NAME}"
source "${BASH_SOURCE%/*}/libs/scale-k8s-deploy.sh"

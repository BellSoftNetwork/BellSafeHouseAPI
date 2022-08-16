#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"
source "${BASH_SOURCE%/*}/libs/check-deploy-requirements.sh"

# CONFIG
export K8s_DEPLOY_REPLICAS="0"
export K8s_DEPLOY_NAMESPACE="${PROJECT_NAMESPACE}"

# Scale
echo "API 스케일 설정 시작"
export K8s_DEPLOY_NAME="${APPLICATION_API_DEPLOYMENT_NAME}"
source "${BASH_SOURCE%/*}/libs/scale-k8s-deploy.sh"

echo "View 스케일 설정 시작"
export K8s_DEPLOY_NAME="${APPLICATION_VIEW_DEPLOYMENT_NAME}"
source "${BASH_SOURCE%/*}/libs/scale-k8s-deploy.sh"

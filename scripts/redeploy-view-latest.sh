#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"
source "${BASH_SOURCE%/*}/libs/check-deploy-requirements.sh"

# CONFIG
export DEPLOYMENT_YAML_PATH="${KUBERNETES_DEPLOY_YAML_PATH}/${APPLICATION_VIEW_DEPLOYMENT_YAML_NAME}"
export APPLICATION_DEPLOYMENT_NAME="${APPLICATION_VIEW_DEPLOYMENT_NAME}"

source "${BASH_SOURCE%/*}/libs/redeploy-k8s-resource.sh"

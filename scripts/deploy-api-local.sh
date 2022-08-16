#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"
source "${BASH_SOURCE%/*}/libs/check-deploy-requirements.sh"

# EXCEPTION
source "${BASH_SOURCE%/*}/libs/check-local-image.sh"

# CONFIG
export DEPLOYMENT_YAML_NAME="${APPLICATION_API_DEPLOYMENT_YAML_NAME}"
export APPLICATION_DEPLOYMENT_NAME="${APPLICATION_API_DEPLOYMENT_NAME}"

source "${BASH_SOURCE%/*}/libs/deploy-k8s-resource.sh"

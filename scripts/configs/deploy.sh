#!/bin/bash

# DEPLOY CONFIG
export PROJECT_NAMESPACE="bell-safe-house-local"

## COMMON
export APPLICATION_NAME="bell-safe-house"
export APPLICATION_URL="bellsafehouse.test"
export COMMON_DEPLOYMENT_YAML_NAME="common-env.yaml"

## API
export API_DEBUG_NODE_PORT="30055"
export API_APPLICATION_DEPLOYMENT_NAME="bell-safe-house-api-local"
export API_DEPLOYMENT_YAML_NAME="api-local-env.yaml"

## VIEW
export VIEW_IMAGE_URL="registry.bellsoft.net/connected-life/bell-safe-house-view/main:latest"
export VIEW_APPLICATION_DEPLOYMENT_NAME="bell-safe-house-view-local"
export VIEW_DEPLOYMENT_YAML_NAME="view-main-latest-env.yaml"

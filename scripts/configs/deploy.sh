#!/bin/bash

# DEPLOY CONFIG
export PROJECT_NAMESPACE="bell-safe-house-local"

## COMMON
export APPLICATION_ENVIRONMENT="local"
export APPLICATION_NAME="${PROJECT_NAME}-${APPLICATION_ENVIRONMENT}"
export APPLICATION_URL="bellsafehouse.test"
export APPLICATION_COMMON_DEPLOYMENT_YAML_NAME="common-env.yaml"

## DB
export DATABASE_MYSQL_SERVICE_NAME="bell-safe-house-mysql"
export DATABASE_MYSQL_VERSION="8.0.30"
export DATABASE_MYSQL_NODE_PORT="30306"
export DATABASE_MYSQL_ROOT_PASSWORD="root"
export DATABASE_MYSQL_DATA_HOST_PATH="/var/bell-safe-house/data/mysql"
export DATABASE_MYSQL_APP_USER="bellsafehouse"
export DATABASE_MYSQL_APP_PASSWORD="bellsafehouse"
export DATABASE_MYSQL_APP_DATABASE="bellsafehouse"
export DATABASE_DEPLOYMENT_YAML_NAME="db-local-env.yaml"

## API
export APPLICATION_API_LATEST_IMAGE_URL="registry.bellsoft.net/connected-life/bell-safe-house-api/main:latest"
export APPLICATION_API_DEBUG_NODE_PORT="30055"
export APPLICATION_API_ENVIRONMENT="${APPLICATION_ENVIRONMENT}"
export APPLICATION_API_NAME="${PROJECT_NAME}-${APPLICATION_API_ENVIRONMENT}-api"
export APPLICATION_API_SERVICE_NAME="${APPLICATION_API_NAME}"
export APPLICATION_API_DEPLOYMENT_NAME="${APPLICATION_API_NAME}"
export APPLICATION_API_DEPLOYMENT_YAML_NAME="api-local-env.yaml"

## VIEW
export APPLICATION_VIEW_LATEST_IMAGE_URL="registry.bellsoft.net/connected-life/bell-safe-house-view/main:latest"
export APPLICATION_VIEW_ENVIRONMENT="${APPLICATION_ENVIRONMENT}"
export APPLICATION_VIEW_NAME="${PROJECT_NAME}-${APPLICATION_VIEW_ENVIRONMENT}-view"
export APPLICATION_VIEW_SERVICE_NAME="${APPLICATION_VIEW_NAME}"
export APPLICATION_VIEW_DEPLOYMENT_NAME="${APPLICATION_VIEW_NAME}"
export APPLICATION_VIEW_DEPLOYMENT_YAML_NAME="view-main-latest-env.yaml"

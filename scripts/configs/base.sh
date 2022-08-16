#!/bin/bash

# BASE COMMAND
export DOCKER="docker"
export KUBECTL="kubectl"
export ENVSUBST="envsubst"


# SYSTEM INFO
export PROJECT_ROOT_PATH="$( cd "${BASH_SOURCE%/*}"/../../ && pwd -P )"
export KUBERNETES_CURRENT_CONTEXT="$( ${KUBECTL} config current-context )"
export KUBERNETES_HOST_MOUNT_PATH_PREFIX="/run/desktop/mnt/host/wsl/ubuntu"
export DOCKER_HOST_MOUNT_PATH_PREFIX="/mnt/wsl/ubuntu"

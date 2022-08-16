#!/bin/bash

# REQUIREMENTS
# - K8s_DEPLOY_NAMESPACE
# - K8s_DEPLOY_NAME
# - K8s_DEPLOY_REPLICAS

OLD_REPLICAS="$(${KUBECTL} -n "${K8s_DEPLOY_NAMESPACE}" get deploy "${K8s_DEPLOY_NAME}" -o=jsonpath='{.status.replicas}')"


# Scale
echo "리소스 replicas 변경 (${OLD_REPLICAS} -> ${K8s_DEPLOY_REPLICAS})"
${KUBECTL} -n "${K8s_DEPLOY_NAMESPACE}" scale deploy "${K8s_DEPLOY_NAME}" --replicas="${K8s_DEPLOY_REPLICAS}"

# Check
${KUBECTL} -n "${K8s_DEPLOY_NAMESPACE}" rollout status deployment "${K8s_DEPLOY_NAME}"

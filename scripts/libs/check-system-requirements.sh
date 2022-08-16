#!/bin/bash

# PRINT CURRENT ENVIRONMENT
echo "-- 현재 시스템 설정 --"
echo "PROJECT_ROOT_PATH: ${PROJECT_ROOT_PATH}"
echo "KUBERNETES_DEFAULT_CONTEXT: ${KUBERNETES_DEFAULT_CONTEXT}"
echo "KUBERNETES_CURRENT_CONTEXT: ${KUBERNETES_CURRENT_CONTEXT}"
echo


# EXCEPTION
echo "-- 개발환경 설정 가능여부 검증 시작 --"

## 권한 검증
if [ "$(id -u)" = "0" ]; then
  echo "관리자 권한으로 전체 스크립트를 실행 시 예기치 못한 문제가 발생할 수 있으므로 실행을 중단합니다."

  exit 1
fi

## 설정값 검증
if [ "${PROJECT_NAMESPACE}" = "" ]; then
  echo "프로젝트 네임스페이스 설정이 누락되었습니다."

  exit 1
else
  echo "프로젝트 네임스페이스 설정 확인 완료"
fi

if [ "${APPLICATION_LOCAL_IMAGE_NAME}" = "" ]; then
  echo "애플리케이션 로컬 이미지 이름 설정이 누락되었습니다."

  exit 1
else
  echo "애플리케이션 로컬 이미지 이름 설정 확인 완료"
fi


## 명령어 검증
if [ "$(which "${DOCKER}")" = "" ]; then
  echo "현재 시스템에서 '${DOCKER}' 명령을 사용할 수 없습니다. 관련 패키지를 설치 또는 권한 설정 후 재시도해주세요."

  exit 1
else
  echo "도커 명령어 확인 완료"
fi

if [ "$(which "${KUBECTL}")" = "" ]; then
  echo "현재 시스템에서 '${KUBECTL}' 명령을 사용할 수 없습니다. 관련 패키지를 설치 또는 권한 설정 후 재시도해주세요."

  exit 1
else
  echo "kubectl 명령어 확인 완료"
fi

if [ "$(which "${ENVSUBST}")" = "" ]; then
  echo "현재 시스템에서 '${ENVSUBST}' 명령을 사용할 수 없습니다. 관련 패키지를 설치 또는 권한 설정 후 재시도해주세요."

  exit 1
else
  echo "envsubst 명령어 확인 완료"
fi


# 환경 검증
if [ "${KUBERNETES_CURRENT_CONTEXT}" != "${KUBERNETES_DEFAULT_CONTEXT}" ]; then
  echo "현재 설정된 k8s context 가 로컬환경이 아닙니다. 변경 후 재시도해주세요."
  echo "필요한 context: '${KUBERNETES_DEFAULT_CONTEXT}', 현재 context: '${KUBERNETES_CURRENT_CONTEXT}'"

  exit 1
else
  echo "k8s context 정상"
fi

echo "-- 개발환경 설정 가능여부 검증 완료 --"
echo

#!/bin/bash

source "${BASH_SOURCE%/*}/configs/base.sh"
source "${BASH_SOURCE%/*}/configs/project.sh"
source "${BASH_SOURCE%/*}/configs/deploy.sh"

source "${BASH_SOURCE%/*}/libs/check-system-requirements.sh"

# Exception
echo "(경고) 로컬 영구볼륨을 제거하면 현재까지 로컬 개발환경에 저장한 모든 데이터가 사라집니다."
echo "정말로 로컬 영구볼륨을 전부 삭제하시겠습니까? (1 or 2)"
select yn in "Yes" "No"; do
  case $yn in
    Yes )
      break;;
    No )
      echo "로컬 영구볼륨 제거 프로세스를 취소합니다."
      echo

      exit 1;
      break;;
  esac
done

# PROCESS
echo "-- 로컬 데이터 제거 시작 --"

## DELETE CACHE
rm -rf .gradle-cache

## DELETE MYSQL PV
${KUBECTL} patch pv "${DATABASE_MYSQL_SERVICE_NAME}-data-pv" -p '{"metadata": {"finalizers": null}}'
${KUBECTL} delete pv "${DATABASE_MYSQL_SERVICE_NAME}-data-pv" --grace-period=0

## DELETE HOST PATH
echo "mysql 데이터 디렉토리 제거에 관리자 권한이 필요하여 1회성 권한을 요청합니다"
if [ "$(sudo id -u)" != "0" ]; then
  echo "관리자 권한을 획득하지 못하여 제거를 중단합니다."
  echo "스크립트를 재 실행하거나 디렉토리를 직접 삭제하세요. ('sudo rm -rf ${DATABASE_MYSQL_DATA_HOST_PATH}/')"

  exit 1
fi

echo "mysql 데이터 디렉토리 제거를 시작합니다."
sudo rm -rf "${DATABASE_MYSQL_DATA_HOST_PATH}/"


echo "-- 로컬 데이터 제거 완료 --"

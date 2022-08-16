#!/bin/bash

echo "프로젝트 린트 설정 시작"
cd "${PROJECT_ROOT_PATH}" || echo "'${PROJECT_ROOT_PATH}' 디렉토리를 찾을 수 없습니다." || exit 1
./gradlew ktlintApplyToIdea
./gradlew addKtlintCheckGitPreCommitHook
cd - || echo "이전 디렉토리로 이동에 실패하였습니다." || exit 1
echo "프로젝트 린트 설정 완료 (적용하려면 IDE 재시작이 필요합니다)"

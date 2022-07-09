## Bell Safe House

안전한 집을 위한 플랫폼

## 프로젝트 환경설정

### 필수 항목 설치

1. IDEA 에 프로젝트 코드 컨벤션 등록: `./gradlew ktlintApplyToIdea`
2. 코드 컨벤션 자동 수정 깃훅 등록: `./gradlew addKtlintCheckGitPreCommitHook`
3. IntelliJ 플러그인 `Save Actions` 설치
4. Save Actions 플러그인에서 `File > Settings > Other Settions > Save Actions`
5. IntelliJ 재시작

### IntelliJ 설정 (`파일 (File)` -> `설정 (Settings)`)

#### 에디터 설정 (`에디터 (Editor)` -> `일반 (General)`)

- `모든 저장된 파일이 줄 바꿈으로 끝나도록 함 (Ensure line feed at file end on Save)` 활성화

#### DevTools 설정 (`빌드, 실행, 배포 (Build, Exeution, Deployment)`)

- `컴파일러 (Compiler)` -> `프로젝트 자동 빌드 (Build project autiomaically)` 활성화
- `고급 설정 (Advanced Settings)`
  -> `개발된 애플리케이션이 현재 실행 중인 경우에도 auto-make가 시작되도록 허용 (Allow auto-make to start even if developed application is currently running)`
  활성화

#### Gradle 설정 (`빌드, 실행, 배포 (Build, Exeution, Deployment)` -> `빌드 도구 (Build Tools)` -> `Gradle`)

- Gradle 프로젝트 내 `다음을 사용하여 빌드 및 실행` 및 `다음을 사용하여 테스트 실행` 을 기존 `Gradle (디폴트)` -> `IntelliJ IDEA` 로 변경

#### 플러그인 설정 (`기타 설정 (Other Settions)`)

##### Save Actions (`Save Actions`)

- `Activate save actions on save` 활성화
- `Optimize imports` 활성화
- `Refomat file` 활성화

### 한글 테스트를 위한 IntelliJ VM 옵션 설정 (`도움말 (Help)` -> `사용자 지정 VM 옵션 편집... (Edit Custom VM Options...)`)

- 하단에 `-Dfile.encoding=UTF-8` 추가

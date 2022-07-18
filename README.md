## Bell Safe House
안전한 집을 위한 플랫폼


## 프로젝트 환경설정
### 필수 설정 구성
1. IDEA 에 프로젝트 코드 컨벤션 등록: `./gradlew ktlintApplyToIdea`
2. 코드 컨벤션 자동 수정 깃훅 등록: `./gradlew addKtlintCheckGitPreCommitHook`
3. IntelliJ 재시작


### IntelliJ 필수 플러그인 설치
- `Save Actions`
- `Kotest`
- `JPA Buddy`
이외 추가 플러그인은 팀 내 Mattermost 추천 리스트 확인


### IntelliJ 기본 설정 (`파일 (File)` -> `설정 (Settings)`)
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
- `Reformat file` 활성화


### IntelliJ 추가 설정
#### IDE 메모리 설정 (`도움말 (Help)` -> `메모리 설정 변경 (Change Memory Settings)`)
또는 Jetbrains Toolbox 내 IntelliJ 설정에서도 변경 가능
- 개인 IDE 사용 패턴 및 PC 사양에 따라 적절히 설정

#### 한글 테스트 설정
##### IntelliJ VM 옵션 설정 (`도움말 (Help)` -> `사용자 지정 VM 옵션 편집... (Edit Custom VM Options...)`)
- 하단에 `-Dfile.encoding=UTF-8` 추가

##### 한글 함수명 작성 시 비 ASCII 문자 경고 제거 (`파일 (File)` -> `설정 (Settings)` -> `검사 (Inspections)`)
- `국제화 (Internationalization)` -> `비 ASCII 문자 (Non-ASCII characters)` -> `테스트` 를 `강조 없이 수정만` 으로 변경 또는 비활성화

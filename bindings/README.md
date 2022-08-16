## bootBuildImag 태스크에 의존성 캐시 등록 방법

1. 각 의존성의 `buildpack.toml` 파일에 있는 `id` 값으로 디렉토리를 생성
2. `type` 파일을 생성하여 내용에 `dependency-mapping` 작성
3. 해당 의존성의 `buildpack.toml` 파일에 정의된 `sha256` 값을 위에서 생성한 디렉토리 내에 파일명으로 생성하고 내용에는 `uri` 값을 등록

### example

#### bellsoft liberica jre

```toml
  [[metadata.dependencies]]
cpes = ["cpe:2.3:a:oracle:jre:17.0.4:*:*:*:*:*:*:*"]
id = "jre"
name = "BellSoft Liberica JRE"
purl = "pkg:generic/bellsoft-jre@17.0.4?arch=amd64"
sha256 = "cf23e85f5a6b4e22007ce2572d38182f535ad22fcf937def8f781181fbb86b65"
stacks = ["io.buildpacks.stacks.bionic", "io.paketo.stacks.tiny", "*"]
uri = "https://github.com/bell-sw/Liberica/releases/download/17.0.4+8/bellsoft-jre17.0.4+8-linux-amd64.tar.gz"
version = "17.0.4"
```

- `buildpack.toml` 파일: https://github.com/paketo-buildpacks/bellsoft-liberica/blob/main/buildpack.toml

1. 파일 내용에 `id` 값인 `jre`을 디렉토리명으로 생성
2. `jre/type` 파일에 `dependency-mapping` 작성
3. `jre/cf23e85f5a6b4e22007ce2572d38182f535ad22fcf937def8f781181fbb86b65` 파일에 대체 다운로드할 URI 작성

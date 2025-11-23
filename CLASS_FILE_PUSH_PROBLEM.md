# .class 파일 푸시 문제 해결 가이드

## 🚨 문제 상황

Eclipse에서 프로젝트를 만들어 `bin/` 폴더의 `.class` 파일까지 Git에 추가했더니 푸시가 안 됩니다.

```
git clone 후에도 푸시가 안 돼요!
```

---

## ✅ 해결 방법 (5분 안에 해결)

### 1단계: .gitignore 파일이 있는지 확인

```bash
cd /path/to/emotion-diary-project
ls -la .gitignore
```

**결과**:
- 파일이 있으면 → 2단계로
- `No such file or directory` → 3단계로

---

### 2단계: .gitignore 파일 확인

```bash
cat .gitignore
```

다음 내용이 있는지 확인:
```
*.class
bin/
```

**있다면**: 4단계로  
**없다면**: 3단계로

---

### 3단계: .gitignore 파일 생성

터미널에서 다음 명령어 실행:

```bash
cat > .gitignore << 'EOF'
# Compiled class files
*.class
bin/
target/
out/

# Eclipse
.classpath
.project
.settings/

# IntelliJ IDEA
.idea/
*.iml
*.iws

# Mac
.DS_Store

# Database
*.db
*.sqlite

# Logs
*.log
EOF
```

또는 직접 파일 생성:
1. 프로젝트 루트에 `.gitignore` 파일 생성
2. 위 내용 복사 & 붙여넣기
3. 저장

---

### 4단계: 이미 추가된 .class 파일 제거

```bash
# Staging에서 제거 (파일은 삭제 안 됨)
git restore --staged bin/
git restore --staged *.class
git restore --staged .classpath
git restore --staged .project

# 또는 모든 것을 unstage
git restore --staged .
```

---

### 5단계: .gitignore와 소스 파일만 추가

```bash
# .gitignore 추가
git add .gitignore

# Java 소스 파일만 추가
git add src/

# 상태 확인
git status
```

**확인 사항**:
- `Changes to be committed`에 `.class` 파일이 없어야 함
- `bin/` 폴더가 없어야 함
- `.java` 파일만 있어야 함

---

### 6단계: 커밋 & 푸시

```bash
# 커밋
git commit -m "fix: .gitignore 추가 및 소스 파일만 커밋"

# 푸시
git push origin <브랜치명>
# 예: git push origin dev
```

---

## 🎯 핵심 요약

### ✅ Git에 올려야 하는 것
- ✅ `.java` 파일 (소스 코드)
- ✅ `.md` 파일 (문서)
- ✅ `.gitignore` 파일
- ✅ `pom.xml` (Maven 프로젝트인 경우)

### ❌ Git에 올리면 안 되는 것
- ❌ `.class` 파일 (컴파일된 파일)
- ❌ `bin/` 폴더 (Eclipse 빌드 폴더)
- ❌ `target/` 폴더 (Maven 빌드 폴더)
- ❌ `.classpath`, `.project` (Eclipse 설정)
- ❌ `.idea/`, `*.iml` (IntelliJ 설정)
- ❌ `.DS_Store` (Mac 시스템 파일)

---

## 🔧 추가 문제 해결

### "이미 푸시한 .class 파일을 제거하고 싶어요"

```bash
# 1. Git에서 제거 (로컬 파일은 유지)
git rm -r --cached bin/
git rm --cached **/*.class

# 2. .gitignore 확인
cat .gitignore | grep -E "bin|class"

# 3. 커밋
git commit -m "remove: .class 파일 및 bin 폴더 제거"

# 4. 푸시
git push origin <브랜치명>
```

---

### "팀원이 이미 .class 파일을 받았어요"

팀원들에게 전달:

```bash
# 1. 최신 코드 받기
git pull origin <브랜치명>

# 2. bin 폴더 삭제
rm -rf bin/

# 3. Eclipse에서 프로젝트 Clean & Build
# Project → Clean → OK
```

---

### "git status에서 bin/ 폴더가 계속 나와요"

```bash
# Git 캐시 초기화
git rm -r --cached .
git add .
git commit -m "fix: Git 캐시 초기화"
```

---

### "푸시는 되는데 느려요"

큰 `.class` 파일들이 이미 히스토리에 있는 경우:

```bash
# 히스토리에서 완전히 제거 (주의: 위험!)
git filter-branch --tree-filter 'rm -rf bin' HEAD

# 또는 BFG Repo-Cleaner 사용 (추천)
# https://rtyley.github.io/bfg-repo-cleaner/
```

**경고**: 히스토리 수정은 위험합니다. 팀원들과 상의 후 진행하세요.

---

## 📚 왜 .class 파일을 Git에 올리면 안 되나요?

### 1. **용량 문제**
- `.class` 파일은 매번 컴파일할 때마다 생성됨
- Git 저장소 크기가 불필요하게 커짐
- 푸시/풀 속도가 느려짐

### 2. **충돌 문제**
- 팀원마다 다른 Java 버전, OS를 사용할 수 있음
- `.class` 파일이 달라서 계속 충돌 발생
- Merge 시 문제 발생

### 3. **보안 문제**
- 컴파일된 코드는 디컴파일 가능
- 소스 코드만 공유하는 것이 안전

### 4. **협업 원칙**
- Git은 **소스 코드 관리** 도구입니다
- 빌드 결과물은 각자 로컬에서 생성합니다
- CI/CD에서 자동으로 빌드합니다

---

## 🎓 올바른 워크플로우

```bash
# 1. 최신 코드 받기
git pull origin <브랜치명>

# 2. Eclipse에서 코드 수정

# 3. 소스 파일만 추가
git add src/

# 4. 커밋
git commit -m "feat: 새 기능 추가"

# 5. 푸시
git push origin <브랜치명>

# 6. 팀원이 받으면 Eclipse에서 자동으로 컴파일됨
```

---

## 💡 Eclipse 설정 팁

### .class 파일이 Git에 안 보이게 하기

1. Eclipse에서 `Project Explorer` 우클릭
2. `Filters and Customization...` 클릭
3. `*.class` 체크
4. `OK` 클릭

이제 Eclipse에서 `.class` 파일이 안 보입니다!

---

## 🆘 그래도 안 되면

1. **FORCE_PUSH_RECOVERY_GUIDE.md** 문서 확인
2. 팀 채팅방에 질문
3. 프로젝트를 다시 클론:

```bash
cd ..
mv emotion-diary-project emotion-diary-project-backup
git clone https://github.com/EmotionDiarySMU/emotion-diary-project.git
cd emotion-diary-project
git checkout <브랜치명>
```

4. 백업 폴더에서 작업한 `.java` 파일만 복사

---

## ✨ 체크리스트

푸시 전에 확인:

- [ ] `.gitignore` 파일이 있나요?
- [ ] `git status`에 `.class` 파일이 없나요?
- [ ] `git status`에 `bin/` 폴더가 없나요?
- [ ] `.java` 파일만 추가했나요?
- [ ] 커밋 메시지를 작성했나요?

모두 체크되었다면 안전하게 푸시할 수 있습니다!

---

**작성**: iee129  
**최종 수정**: 2025년 11월 23일  
**관련 문서**: FORCE_PUSH_RECOVERY_GUIDE.md

> 💡 **중요**: Git에는 항상 **소스 코드(.java)만** 올립니다. 컴파일된 파일(.class)은 각자 빌드합니다!


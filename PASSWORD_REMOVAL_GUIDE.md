# Git 히스토리에서 비밀번호 제거 가이드

## 🚨 상황 분석

현재 `stats` 브랜치에 비밀번호(`REMOVED_PASSWORD`)가 포함된 커밋이 있습니다.

**영향받은 파일:**
- `src/main/java/com/diary/emotion/DatabaseManager.java`
- `src/main/java/com/diary/emotion/Main.java`

---

## 💡 3가지 해결 방법

### 방법 1: BFG Repo-Cleaner (가장 빠르고 쉬움) ⭐ 추천

```bash
# 1. 스크립트 실행 권한 부여
chmod +x remove_password_simple.sh

# 2. 스크립트 실행
./remove_password_simple.sh

# 3. 원격 저장소에 반영
git push origin stats --force
```

**장점:**
- ✅ 가장 빠름 (몇 초 안에 완료)
- ✅ 자동으로 모든 커밋 처리
- ✅ 안전한 백업 자동 생성

---

### 방법 2: git-filter-repo (세밀한 제어)

```bash
# 1. 스크립트 실행 권한 부여
chmod +x remove_password_from_history.sh

# 2. 스크립트 실행 (대화형)
./remove_password_from_history.sh

# 3. 안내에 따라 진행
```

**장점:**
- ✅ 더 정교한 제어 가능
- ✅ 단계별 확인 가능

---

### 방법 3: 수동 방법 (학습용)

#### 3-1. 백업 생성
```bash
git branch backup-manual-$(date +%Y%m%d)
```

#### 3-2. BFG 설치
```bash
brew install bfg
```

#### 3-3. 비밀번호 교체 파일 생성
```bash
echo "REMOVED_PASSWORD" > passwords.txt
```

#### 3-4. BFG 실행
```bash
bfg --replace-text passwords.txt .
```

#### 3-5. Git 정리
```bash
git reflog expire --expire=now --all
git gc --prune=now --aggressive
```

#### 3-6. 검증
```bash
git log --all -p | grep "U9Bsi7sj1"
```

결과가 없으면 성공!

#### 3-7. 원격 저장소 업데이트
```bash
git push origin stats --force
```

---

## ⚠️ Force Push 전 필수 확인사항

### 1. 현재 stats 브랜치 상태 확인
```bash
git log stats --oneline
```

### 2. 원격 저장소 상태 확인
```bash
git fetch origin
git log origin/stats --oneline
```

### 3. 팀원 확인
- 다른 팀원이 stats 브랜치를 사용 중인가?
- **Yes** → 팀원에게 먼저 알려주세요!
- **No** → 안전하게 진행 가능

---

## 🔄 팀원이 있는 경우

### 1. 팀원에게 알림
```
안녕하세요!
stats 브랜치의 Git 히스토리를 수정해야 합니다.
(보안상의 이유로 비밀번호 제거)

작업이 완료되면 다음과 같이 재동기화해주세요:

git fetch origin
git reset --hard origin/stats
```

### 2. Force Push 실행
```bash
git push origin stats --force
```

### 3. 팀원 재동기화 확인

---

## ✅ 성공 확인 방법

### 1. 로컬 히스토리 확인
```bash
# 비밀번호 검색 (결과가 없어야 함)
git log --all -p | grep "U9Bsi7sj1"

# 교체된 텍스트 확인 (BFG 사용 시)
git log --all -p | grep "REMOVED"
```

### 2. 원격 저장소 확인
```bash
# 원격 저장소 최신화
git fetch origin

# 원격 히스토리 확인
git log origin/stats -p | grep "U9Bsi7sj1"
```

### 3. GitHub에서 확인
- GitHub 저장소 → stats 브랜치로 이동
- 파일 히스토리 확인
- DatabaseManager.java의 전체 커밋 히스토리 확인

---

## 🔐 작업 완료 후 할 일

### 1. 로컬 작업 파일 비밀번호 복원

히스토리에서는 제거되었지만, 로컬에서는 실제 비밀번호를 사용해야 합니다.

```bash
# DatabaseManager.java 수정
# DB_PW를 REMOVED_PASSWORD로 다시 설정
```

### 2. .gitignore에 BEFORE_PUSH.md 추가

```bash
echo "docs/BEFORE_PUSH.md" >> .gitignore
git add .gitignore
git commit -m "chore: Add BEFORE_PUSH.md to .gitignore"
```

### 3. MySQL 비밀번호 변경 (선택사항)

더 안전하게 하려면 MySQL 비밀번호도 변경:

```sql
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY '새로운_비밀번호';
FLUSH PRIVILEGES;
```

그리고 코드의 DB_PW도 새 비밀번호로 변경.

---

## 📊 작업 체크리스트

### 준비 단계
- [ ] 현재 작업 내용 커밋 또는 stash
- [ ] 백업 브랜치 생성 확인
- [ ] 팀원에게 알림 (필요 시)

### 실행 단계
- [ ] BFG 또는 git-filter-repo 설치
- [ ] 비밀번호 제거 스크립트 실행
- [ ] 로컬 히스토리 검증
- [ ] Force push 실행

### 완료 단계
- [ ] 원격 히스토리 검증
- [ ] 로컬 작업 파일 비밀번호 복원
- [ ] .gitignore 업데이트
- [ ] 백업 브랜치 삭제 (성공 확인 후)

---

## 🆘 문제 해결

### "git-filter-repo가 없습니다"
```bash
brew install git-filter-repo
```

### "BFG를 찾을 수 없습니다"
```bash
brew install bfg
```

### "force push가 거부되었습니다"
```bash
# 보호된 브랜치인 경우
# GitHub Settings → Branches → Branch protection rules 확인
```

### "변경사항이 사라졌습니다"
```bash
# 백업 브랜치로 복구
git checkout backup-manual-YYYYMMDD
```

---

## 💡 빠른 시작

**가장 빠른 방법 (1분 안에 완료):**

```bash
# 1. BFG 설치
brew install bfg

# 2. 백업
git branch backup-now

# 3. 비밀번호 제거
echo "REMOVED_PASSWORD" > /tmp/pwd.txt
bfg --replace-text /tmp/pwd.txt .

# 4. 정리
git reflog expire --expire=now --all
git gc --prune=now --aggressive

# 5. 확인
git log --all -p | grep "U9Bsi7sj1"

# 6. Force push
git push origin stats --force

# 7. 완료!
```

---

## 📞 추가 도움이 필요하면

1. **로그 확인:**
   ```bash
   cat /tmp/filter-repo.log
   ```

2. **백업 확인:**
   ```bash
   git branch | grep backup
   ```

3. **현재 상태 확인:**
   ```bash
   git status
   git log --oneline -5
   ```

---

**작업 시작 전 꼭 백업하세요!** 🔒


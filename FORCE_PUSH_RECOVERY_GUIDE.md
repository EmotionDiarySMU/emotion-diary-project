# 강제 푸시 문제 해결 완벽 가이드

**작성일**: 2025년 11월 23일  
**문제**: 다른 팀원이 작업 중인 브랜치에 강제 푸시(force push)를 해서 팀원이 푸시를 못하게 된 상황

---

## 📋 목차

1. [문제 상황 이해](#1-문제-상황-이해)
2. [즉시 해결 방법 (팀원용)](#2-즉시-해결-방법-팀원용)
3. [상황별 해결 시나리오](#3-상황별-해결-시나리오)
4. [관리자가 브랜치 복구하기](#4-관리자가-브랜치-복구하기)
5. [예방 방법](#5-예방-방법)
6. [Git 명령어 설명](#6-git-명령어-설명)
7. [자주 묻는 질문 (FAQ)](#7-자주-묻는-질문-faq)

---

## 1. 문제 상황 이해

### 무슨 일이 발생했나요?

다른 팀원이 작업 중인 브랜치에 **강제 푸시(force push)**가 발생하면:

```bash
git push --force origin <브랜치명>
```

이 명령은 원격 브랜치의 히스토리를 **완전히 덮어씁니다**.

### 증상

- 팀원이 `git push`를 할 때 거부됨
- "Updates were rejected" 에러 메시지
- `git pull` 해도 충돌 발생
- 로컬에 있던 커밋이 원격에 없음

### 왜 발생하나요?

1. **히스토리 불일치**: 로컬 브랜치와 원격 브랜치의 커밋 히스토리가 달라짐
2. **커밋 손실**: 강제 푸시로 일부 커밋이 원격에서 제거됨
3. **동기화 실패**: Git이 안전하게 merge할 수 없는 상태

---

## 2. 즉시 해결 방법 (팀원용)

### 🚀 방법 1: 작업 내용 백업 후 동기화 (가장 안전)

**추천 대상**: 작업 내용을 잃고 싶지 않은 경우

```bash
# 1단계: 현재 작업 중인 내용 백업
git stash save "작업 중인 내용 백업 - $(date +%Y%m%d-%H%M%S)"

# 2단계: 원격 저장소 최신 정보 가져오기
git fetch origin

# 3단계: 현재 브랜치 확인
git branch

# 4단계: 로컬 브랜치를 원격과 강제로 동기화
git reset --hard origin/<브랜치명>
# 예: git reset --hard origin/View

# 5단계: 백업한 작업 내용 복구
git stash pop

# 6단계: 충돌 해결 (있다면)
# 충돌이 발생하면 파일을 수동으로 수정

# 7단계: 작업 내용 커밋
git add .
git commit -m "작업 내용 재적용"

# 8단계: 푸시
git push origin <브랜치명>
```

**주의사항**:
- `git stash pop` 시 충돌이 발생할 수 있습니다
- 충돌 발생 시 파일을 열어서 `<<<<<<<`, `=======`, `>>>>>>>` 부분을 수동으로 수정하세요

---

### 🔄 방법 2: 파일 복사 방식 (가장 간단)

**추천 대상**: Git 초보자, 작업 파일이 많지 않은 경우

```bash
# 1단계: 작업 중인 파일들을 바탕화면 등 다른 곳에 복사
# (파일 탐색기/Finder에서 수동으로 복사)

# 2단계: Git 동기화
git fetch origin
git reset --hard origin/<브랜치명>

# 3단계: 복사한 파일을 프로젝트 폴더에 다시 붙여넣기

# 4단계: 커밋 및 푸시
git add .
git commit -m "작업 내용 재적용"
git push origin <브랜치명>
```

---

### 🔧 방법 3: 새 브랜치 생성 (작업이 복잡한 경우)

**추천 대상**: 작업 내용이 많고 복잡한 경우

```bash
# 1단계: 현재 작업을 새 브랜치에 백업
git branch backup-<브랜치명>-$(date +%Y%m%d)
# 예: git branch backup-View-20251123

# 2단계: 원격과 동기화
git fetch origin
git reset --hard origin/<브랜치명>

# 3단계: 백업 브랜치의 변경사항을 현재 브랜치에 적용
git cherry-pick <백업브랜치의_커밋해시>
# 또는 여러 커밋을 한 번에:
git cherry-pick <시작커밋>..<끝커밋>

# 4단계: 푸시
git push origin <브랜치명>
```

---

### 📦 방법 4: 저장소 새로 클론 (최후의 수단)

**추천 대상**: 위 방법이 모두 실패한 경우

```bash
# 1단계: 현재 프로젝트 폴더 이름 변경
cd ..
mv emotion-diary-project emotion-diary-project-backup

# 2단계: 저장소 새로 클론
git clone https://github.com/EmotionDiarySMU/emotion-diary-project.git

# 3단계: 작업 브랜치로 전환
cd emotion-diary-project
git checkout <브랜치명>

# 4단계: 백업 폴더에서 작업 파일 복사
# (백업 폴더의 src/ 등을 새 폴더로 복사)

# 5단계: 커밋 및 푸시
git add .
git commit -m "작업 내용 재적용"
git push origin <브랜치명>
```

---

## 3. 상황별 해결 시나리오

### 시나리오 A: "src 폴더를 pull로 가져올 수 없어요"

**증상**:
```bash
git pull origin <브랜치명>
# 완료되지만 src 폴더가 없음
```

**해결 방법**:

```bash
# 1. 원격에 src가 정말 있는지 확인
git ls-tree -r origin/<브랜치명> --name-only | grep src

# 결과가 없다면 → 원격에 src가 없는 것
# 결과가 있다면 → 아래 계속 진행

# 2. 로컬 캐시 완전히 제거
git rm -r --cached .

# 3. 원격과 강제 동기화
git reset --hard origin/<브랜치명>

# 4. 깨끗하게 정리
git clean -fdx  # 주의: 추적되지 않는 모든 파일 삭제

# 5. 다시 확인
ls -la src/
```

**여전히 안 되면**:
```bash
# 저장소 새로 클론
cd ..
rm -rf emotion-diary-project
git clone https://github.com/EmotionDiarySMU/emotion-diary-project.git
cd emotion-diary-project
git checkout <브랜치명>
ls -la src/
```

---

### 시나리오 B: "git push가 계속 거부돼요"

**증상**:
```bash
git push origin <브랜치명>
# ! [rejected] <브랜치명> -> <브랜치명> (non-fast-forward)
```

**해결 방법**:

```bash
# 1. 작업 백업
git stash save "임시 백업"

# 2. 원격과 동기화
git fetch origin
git reset --hard origin/<브랜치명>

# 3. 백업 복구
git stash pop

# 4. 다시 커밋
git add .
git commit -m "작업 재적용"

# 5. 푸시
git push origin <브랜치명>
```

---

### 시나리오 C: "git pull을 하면 merge 충돌이 발생해요"

**증상**:
```bash
git pull origin <브랜치명>
# CONFLICT (content): Merge conflict in <파일명>
```

**해결 방법 1 - 충돌 해결**:

```bash
# 1. 충돌 파일 확인
git status

# 2. 충돌 파일 열기 (에디터에서)
# 파일 안에 다음과 같은 표시가 있음:
# <<<<<<< HEAD
# 내 코드
# =======
# 원격 코드
# >>>>>>> origin/<브랜치명>

# 3. 원하는 코드만 남기고 나머지 삭제
# (충돌 표시 <<<<, ====, >>>> 도 삭제)

# 4. 충돌 해결 완료 표시
git add <파일명>

# 5. 커밋
git commit -m "충돌 해결"

# 6. 푸시
git push origin <브랜치명>
```

**해결 방법 2 - 충돌 무시하고 원격 우선**:

```bash
# pull 취소
git merge --abort

# 원격 것으로 덮어쓰기
git fetch origin
git reset --hard origin/<브랜치명>

# 작업 다시 시작
```

---

### 시나리오 D: "이미 커밋했는데 push가 안 돼요"

**증상**:
```bash
git commit -m "작업 완료"
git push origin <브랜치명>
# ! [rejected]
```

**해결 방법**:

```bash
# 1. 커밋 해시 기록
git log --oneline -5
# 맨 위의 커밋 해시를 메모장에 복사 (예: abc1234)

# 2. 원격과 동기화
git fetch origin
git reset --hard origin/<브랜치명>

# 3. 이전 커밋 다시 적용
git cherry-pick abc1234
# (abc1234는 1단계에서 복사한 해시)

# 4. 푸시
git push origin <브랜치명>
```

---

### 시나리오 E: "여러 명이 동시에 작업 중이에요"

**해결 방법**:

```bash
# 1. 모든 팀원이 작업 백업
git stash save "백업"

# 2. 관리자가 브랜치 상태 확인 후 복구
# (섹션 4 참고)

# 3. 모든 팀원이 동기화
git fetch origin
git reset --hard origin/<브랜치명>

# 4. 각자 작업 복구
git stash pop

# 5. 작업 순서 정하기
# 팀원 A → 커밋 & 푸시
# 팀원 B → pull → 커밋 & 푸시
# 팀원 C → pull → 커밋 & 푸시
```

---

## 4. 관리자가 브랜치 복구하기

### 상황: 강제 푸시로 팀원의 커밋이 사라진 경우

### 4-1. Reflog로 잃어버린 커밋 찾기

```bash
# 1. 원격 저장소 reflog 확인
git fetch origin
git reflog show origin/<브랜치명> --date=iso

# 출력 예시:
# abc1234 origin/<브랜치명>@{2025-11-23 01:00:00 +0900}: update by push
# def5678 origin/<브랜치명>@{2025-11-22 23:00:00 +0900}: update by push

# 2. 팀원의 마지막 커밋 찾기
git log --all --format="%h %an %s %ad" --date=short | grep <팀원이름>

# 3. 해당 커밋 해시 확인 (예: def5678)
```

---

### 4-2. Git Revert로 안전하게 되돌리기 (추천)

**장점**: 히스토리 보존, 안전, 협업 친화적

```bash
# 1. 되돌릴 브랜치로 전환
git checkout <브랜치명>
git pull origin <브랜치명>

# 2. 되돌리고 싶은 커밋 범위 확인
git log --oneline

# 출력 예시:
# abc1234 (HEAD) 강제 푸시된 커밋
# def5678 팀원의 커밋 (여기로 돌아가고 싶음)
# ghi9012 이전 커밋

# 3. def5678 이후의 모든 커밋을 revert
git revert --no-commit def5678..HEAD

# 4. revert 커밋 생성
git commit -m "Revert: 강제 푸시 이전 상태로 복구"

# 5. 푸시 (강제 푸시 아님!)
git push origin <브랜치명>
```

---

### 4-3. Reset으로 복구하기 (비추천 - 최후의 수단)

**단점**: 히스토리 삭제, 위험, 협업 방해

```bash
# 1. 브랜치로 전환
git checkout <브랜치명>

# 2. 복구할 커밋으로 reset
git reset --hard def5678

# 3. 강제 푸시 (주의!)
git push --force origin <브랜치명>

# 4. 팀원들에게 알리기 - 모두 동기화 필요
# Slack/Discord에 메시지:
# "긴급: <브랜치명> 브랜치를 복구했습니다.
#  모두 git fetch origin && git reset --hard origin/<브랜치명> 실행해주세요!"
```

---

### 4-4. 여러 팀원의 커밋 복구하기

```bash
# 1. 각 팀원의 커밋 찾기
git log --all --format="%h %an %s" | grep -E "팀원1|팀원2|팀원3"

# 2. 현재 브랜치 상태 확인
git log --oneline -10

# 3. Cherry-pick으로 필요한 커밋들만 가져오기
git cherry-pick <커밋해시1>
git cherry-pick <커밋해시2>
git cherry-pick <커밋해시3>

# 4. 푸시
git push origin <브랜치명>
```

---

## 5. 예방 방법

### 5-1. Branch Protection Rules 설정 (GitHub)

GitHub 저장소에서:

1. **Settings** → **Branches** 클릭
2. **Add branch protection rule** 클릭
3. **Branch name pattern**: `main`, `master`, `develop` 등 입력
4. 다음 옵션 체크:
   - ✅ **Require pull request reviews before merging**
   - ✅ **Require status checks to pass before merging**
   - ✅ **Require conversation resolution before merging**
   - ✅ **Do not allow bypassing the above settings**
   - ✅ **Restrict who can push to matching branches**
5. **Create** 클릭

**효과**: 강제 푸시가 차단됨

---

### 5-2. Git Hooks 설정 (로컬)

프로젝트 루트에 `.git/hooks/pre-push` 파일 생성:

```bash
#!/bin/bash
# 강제 푸시 방지 훅

protected_branches="main master develop View write login stats"
current_branch=$(git symbolic-ref HEAD | sed -e 's,.*/\(.*\),\1,')

# 강제 푸시 감지
if [[ "$@" == *"--force"* ]] || [[ "$@" == *"-f"* ]]; then
    if [[ "$protected_branches" == *"$current_branch"* ]]; then
        echo "❌ 에러: $current_branch 브랜치에 강제 푸시가 금지되어 있습니다!"
        echo "팀원과 상의 후 진행하세요."
        exit 1
    fi
fi

exit 0
```

실행 권한 부여:
```bash
chmod +x .git/hooks/pre-push
```

---

### 5-3. Git Alias 설정

안전한 명령어 별칭 만들기:

```bash
# ~/.gitconfig 파일에 추가
git config --global alias.sync '!git fetch origin && git pull origin $(git rev-parse --abbrev-ref HEAD)'
git config --global alias.safe-push '!git pull origin $(git rev-parse --abbrev-ref HEAD) && git push origin $(git rev-parse --abbrev-ref HEAD)'
git config --global alias.force-push '!echo "경고: 강제 푸시는 위험합니다. 정말 실행하시겠습니까? (yes/no)" && read confirm && [ "$confirm" = "yes" ] && git push --force-with-lease origin $(git rev-parse --abbrev-ref HEAD)'
```

사용:
```bash
git sync        # 안전한 pull
git safe-push   # pull 후 push
git force-push  # 확인 후 강제 푸시
```

---

### 5-4. 팀 규칙 정하기

**협업 규칙 문서 작성 (COLLABORATION_RULES.md)**:

```markdown
# 팀 협업 규칙

## ❌ 절대 금지
1. `git push --force` 사용 금지
2. 다른 팀원의 브랜치에 직접 푸시 금지
3. `main`, `master` 브랜치에 직접 커밋 금지

## ✅ 권장 사항
1. 항상 자신의 브랜치에서 작업
2. PR(Pull Request)로 코드 리뷰 후 merge
3. 작업 시작 전 `git pull` 필수
4. 작업 후 자주 커밋 & 푸시

## 🆘 문제 발생 시
1. 팀 채팅방에 즉시 알리기
2. FORCE_PUSH_RECOVERY_GUIDE.md 참고
3. 관리자에게 도움 요청
```

---

### 5-5. pull.rebase 설정

```bash
# Merge 방식 (안전, 추천)
git config --global pull.rebase false

# 또는 프로젝트별로
cd /path/to/project
git config pull.rebase false
```

**설정 후**: `git pull` 시 자동으로 merge commit 생성

---

## 6. Git 명령어 설명

### 기본 명령어

| 명령어 | 설명 | 안전성 |
|--------|------|--------|
| `git fetch origin` | 원격 정보만 가져오기 (로컬 변경 안 함) | ✅ 안전 |
| `git pull origin <브랜치>` | 원격 내용을 가져와서 merge | ⚠️ 충돌 가능 |
| `git push origin <브랜치>` | 로컬 커밋을 원격에 업로드 | ✅ 안전 |
| `git push --force` | 원격 브랜치 강제 덮어쓰기 | ❌ 위험 |

---

### 작업 백업 명령어

| 명령어 | 설명 | 사용 시점 |
|--------|------|-----------|
| `git stash save "메시지"` | 현재 변경사항 임시 저장 | 브랜치 전환 전 |
| `git stash list` | 저장된 stash 목록 확인 | - |
| `git stash pop` | 가장 최근 stash 적용 & 삭제 | 작업 복구 시 |
| `git stash apply` | stash 적용 (삭제 안 함) | 여러 브랜치에 적용 |
| `git stash drop` | stash 삭제 | 더 이상 필요 없을 때 |

---

### 히스토리 조작 명령어

| 명령어 | 설명 | 히스토리 변경 |
|--------|------|--------------|
| `git reset --soft <커밋>` | HEAD만 이동, 변경사항 유지 | ✅ 변경 |
| `git reset --mixed <커밋>` | HEAD & Index 이동, 작업 디렉토리 유지 | ✅ 변경 |
| `git reset --hard <커밋>` | 모든 것을 커밋 상태로 되돌림 | ✅ 변경 |
| `git revert <커밋>` | 커밋을 취소하는 새 커밋 생성 | ❌ 보존 |
| `git cherry-pick <커밋>` | 특정 커밋만 현재 브랜치에 적용 | ❌ 보존 |

---

### 정보 확인 명령어

| 명령어 | 설명 | 예시 |
|--------|------|------|
| `git status` | 현재 상태 확인 | - |
| `git log --oneline` | 커밋 히스토리 간략히 | `git log --oneline -10` |
| `git log --all --graph` | 모든 브랜치 시각화 | - |
| `git reflog` | 로컬 HEAD 이동 기록 | 잃어버린 커밋 찾기 |
| `git ls-tree -r <브랜치>` | 브랜치의 파일 목록 | `git ls-tree -r origin/main` |
| `git branch -r` | 원격 브랜치 목록 | - |
| `git remote -v` | 원격 저장소 URL 확인 | - |

---

### 브랜치 관리 명령어

| 명령어 | 설명 | 사용 예시 |
|--------|------|-----------|
| `git branch` | 로컬 브랜치 목록 | - |
| `git branch <이름>` | 새 브랜치 생성 | `git branch backup-View` |
| `git checkout <브랜치>` | 브랜치 전환 | `git checkout main` |
| `git checkout -b <브랜치>` | 생성 & 전환 | `git checkout -b feature-x` |
| `git branch -d <브랜치>` | 브랜치 삭제 (안전) | `git branch -d old-branch` |
| `git branch -D <브랜치>` | 브랜치 강제 삭제 | - |

---

## 7. 자주 묻는 질문 (FAQ)

### Q1: git pull을 했는데도 src 폴더가 안 보여요

**A**: 다음을 확인하세요:

```bash
# 1. 원격에 정말 src가 있는지 확인
git ls-tree -r origin/<브랜치명> --name-only | grep src

# 2. .gitignore 확인
cat .gitignore | grep src

# 3. 서브모듈 확인
git submodule status

# 4. 강제 동기화
git fetch origin
git reset --hard origin/<브랜치명>
```

여전히 없다면 → 원격 브랜치에 src가 커밋되지 않은 것입니다.

---

### Q2: git stash pop 했는데 충돌이 나요

**A**: 

```bash
# 1. 충돌 파일 확인
git status

# 2. 충돌 파일 열어서 수동 수정
# <<<<<<<, =======, >>>>>>> 표시 제거

# 3. 해결 완료
git add <파일명>

# 4. stash 삭제
git stash drop
```

---

### Q3: 강제 푸시를 실수로 했어요. 되돌릴 수 있나요?

**A**: 가능합니다!

```bash
# 1. Reflog 확인
git reflog show origin/<브랜치명>

# 2. 강제 푸시 이전 커밋 찾기 (시간 확인)
# 예: abc1234 (강제 푸시 이전)

# 3. 복구
git checkout <브랜치명>
git reset --hard abc1234
git push --force origin <브랜치명>

# 4. 팀원들에게 알리기!
```

**주의**: Reflog는 로컬에만 있으므로, 강제 푸시한 컴퓨터에서만 복구 가능합니다.

---

### Q4: 여러 명이 동시에 같은 파일을 수정했어요

**A**: Pull → 충돌 해결 → Push 순서로:

```bash
# 팀원 A
git pull origin <브랜치명>
# 충돌 해결
git add .
git commit -m "충돌 해결"
git push origin <브랜치명>

# 팀원 B (A가 푸시한 후)
git pull origin <브랜치명>
# 충돌 해결
git add .
git commit -m "충돌 해결"
git push origin <브랜치명>
```

---

### Q5: git reset --hard를 실수로 했어요. 작업이 날아갔어요

**A**: Reflog로 복구 가능:

```bash
# 1. HEAD 이동 기록 확인
git reflog

# 출력 예시:
# abc1234 HEAD@{0}: reset: moving to abc1234
# def5678 HEAD@{1}: commit: 작업 완료  ← 여기로 돌아가고 싶음
# ghi9012 HEAD@{2}: commit: 이전 작업

# 2. 복구
git reset --hard def5678

# 3. 확인
git log --oneline
```

**주의**: Reflog는 커밋된 내용만 복구 가능합니다. 커밋하지 않은 변경사항은 복구 불가능합니다.

---

### Q6: main 브랜치와 master 브랜치가 모두 있어요

**A**: 프로젝트에 따라 다릅니다:

```bash
# 1. 어느 브랜치가 기본인지 확인
git remote show origin

# 출력에서 "HEAD branch: main" 또는 "HEAD branch: master" 확인

# 2. 기본 브랜치를 사용하세요
# GitHub 최신 프로젝트 → main
# 오래된 프로젝트 → master
```

---

### Q7: 커밋 메시지를 잘못 썼어요

**A**: 

**아직 푸시 안 했다면**:
```bash
git commit --amend -m "올바른 메시지"
```

**이미 푸시했다면**:
```bash
# 메시지만 변경하는 새 커밋 추가
git commit --allow-empty -m "이전 커밋 메시지 수정: 올바른 내용"
git push origin <브랜치명>
```

---

### Q8: 브랜치 이름을 바꾸고 싶어요

**A**:

```bash
# 1. 로컬 브랜치 이름 변경
git branch -m <옛이름> <새이름>

# 2. 원격의 옛 브랜치 삭제
git push origin --delete <옛이름>

# 3. 새 이름으로 푸시
git push origin <새이름>

# 4. 업스트림 설정
git push --set-upstream origin <새이름>
```

---

### Q9: 커밋을 취소하고 싶어요 (아직 푸시 안 함)

**A**:

```bash
# 방법 1: 커밋 취소, 변경사항 유지
git reset --soft HEAD~1

# 방법 2: 커밋 취소, Staged 해제, 변경사항 유지
git reset --mixed HEAD~1

# 방법 3: 커밋 취소, 변경사항도 삭제
git reset --hard HEAD~1
```

---

### Q10: 다른 사람의 커밋을 내 브랜치에 가져오고 싶어요

**A**:

```bash
# 1. 원격 정보 업데이트
git fetch origin

# 2. 특정 커밋만 가져오기
git cherry-pick <커밋해시>

# 또는 다른 브랜치의 특정 파일만
git checkout <브랜치명> -- <파일경로>
# 예: git checkout View -- src/view/ViewDiaryPanel.java
```

---

## 🎯 핵심 요약

### ✅ 해야 할 것
1. 작업 전 **항상 git pull**
2. 자주 커밋하고 푸시하기
3. 문제 발생 시 **즉시 팀에 알리기**
4. **git stash**로 작업 백업하기

### ❌ 하지 말아야 할 것
1. **git push --force** 사용하지 않기
2. 다른 사람의 브랜치에 직접 푸시하지 않기
3. 커밋하지 않은 채로 브랜치 전환하지 않기
4. .git 폴더 직접 수정하지 않기

### 🆘 문제 발생 시 우선순위
1. **작업 백업** (`git stash` 또는 파일 복사)
2. **원격과 동기화** (`git fetch` + `git reset --hard`)
3. **작업 복구** (`git stash pop` 또는 파일 붙여넣기)
4. **커밋 & 푸시**

---

## 📞 도움이 필요하면

1. 이 문서의 시나리오를 먼저 확인
2. FAQ 섹션 확인
3. 팀 채팅방에 질문
4. GitHub Issues에 문제 상황 공유
5. Git 공식 문서: https://git-scm.com/doc

---

**작성**: iee129  
**최종 수정**: 2025년 11월 23일  
**버전**: 1.0

> 💡 **팁**: 이 문서를 북마크하고, 문제 발생 시 즉시 참고하세요!


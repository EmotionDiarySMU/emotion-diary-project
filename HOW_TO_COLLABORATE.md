# 🤝 팀 협업 가이드 - 초보자용

**작성일**: 2025년 11월 22일  
**대상**: 감정 일기장 프로젝트 팀원 전체

---

## ⚠️ 중요: 절대 하지 말아야 할 것

### 🚫 절대 금지!

1. **master 브랜치에 직접 푸시하지 마세요!**
   ```bash
   # ❌ 이런 짓 하지 마세요!
   git checkout master
   git add .
   git commit -m "수정"
   git push origin master  # ← 이거 안 됩니다!
   ```

2. **다른 사람의 브랜치 코드를 수동으로 복사하지 마세요!**
   - 복사-붙여넣기로 코드 합치지 말기
   - 파일 직접 옮기지 말기
   - GitHub에서 직접 코드 합치지 말기

---

## ✅ 올바른 작업 방법

### 1단계: 자기 브랜치에서만 작업하기

각자 담당 브랜치:
- **로그인 담당**: `login` 브랜치
- **일기 작성 담당**: `write` 브랜치
- **일기 열람 담당**: `View` 브랜치
- **통계 담당**: `stats` 브랜치

**실제로 해야 할 것:**

```bash
# 1. 자기 브랜치로 이동
git checkout login  # 자기 담당 브랜치명 (write, View, stats 중 하나)

# 2. 최신 코드 받기
git pull origin login

# 3. IntelliJ에서 코드 수정
# (파일 열어서 편집하기)

# 4. 수정한 내용 저장
git add .
git commit -m "feat: 로그인 버그 수정"  # 뭘 했는지 간단히 적기

# 5. GitHub에 올리기
git push origin login
```

---

### 2단계: Pull Request (PR) 만들기

**PR이 뭔가요?** 
→ "내가 수정한 코드를 master에 합쳐주세요!"라고 요청하는 것

**어떻게 하나요?**

#### 방법 1: GitHub 웹사이트에서 (제일 쉬움!)

1. 코드를 푸시하면 터미널에 링크가 나옴:
   ```
   remote: Create a pull request for 'login' on GitHub by visiting:
   remote:   https://github.com/EmotionDiarySMU/emotion-diary-project/pull/new/login
   ```

2. 그 링크를 브라우저에서 열기

3. 화면에 나오는 대로:
   - 제목: "로그인 기능 개선" (뭘 했는지 적기)
   - 설명: "비밀번호 검증 로직 수정" (간단히 설명)
   - **Create pull request** 버튼 클릭!

4. 끝! 이제 팀장/팀원들이 코드 확인 후 master에 합쳐줌

#### 방법 2: 터미널에서 (한 줄로!)

```bash
# GitHub CLI 사용 (처음 한 번만 설치)
brew install gh
gh auth login

# PR 생성 (제목과 내용 자동으로!)
gh pr create --base master --head login --fill
```

---

### 3단계: 코드 리뷰 및 병합

1. **PR 생성 후**: 팀원들에게 알리기
2. **리뷰**: 다른 팀원이 코드 확인
3. **승인**: 문제 없으면 "Approve" 클릭
4. **병합**: GitHub에서 **Merge pull request** 버튼 클릭
5. **완료**: 자동으로 master에 합쳐짐!

---

## 🔄 다른 팀원 코드 가져오기

**상황**: 다른 팀원이 master에 새 코드를 올렸어요. 내 브랜치에도 반영하고 싶어요.

**올바른 방법:**

```bash
# 1. 현재 작업 저장
git add .
git commit -m "작업 중 저장"

# 2. master 최신 코드 받기
git checkout master
git pull origin master

# 3. 자기 브랜치로 돌아오기
git checkout login  # 자기 브랜치명

# 4. master 코드 합치기
git merge master

# 5. 충돌 있으면 해결 (IntelliJ가 도와줌)
# IntelliJ에서 충돌 파일 열기 → Accept Yours/Accept Theirs 선택

# 6. 완료 후 푸시
git add .
git commit -m "merge: master 최신 코드 반영"
git push origin login
```

---

## ❓ 자주 묻는 질문 (FAQ)

### Q1: "코드가 겹치면 어떻게 해요?"
**A**: Git이 자동으로 합쳐줍니다. 충돌나면 IntelliJ가 화면에 보여줌 → 어느 걸 쓸지 선택만 하면 됨

### Q2: "실수로 master에 푸시하려고 했는데 안 돼요!"
**A**: 정상입니다! 보호 기능이 작동 중. 자기 브랜치로 가서 푸시하세요.

### Q3: "다른 팀원 코드를 내 브랜치에 넣고 싶어요"
**A**: 위의 "다른 팀원 코드 가져오기" 따라하기. **절대 복사-붙여넣기 하지 말 것!**

### Q4: "PR을 만들었는데 뭘 해야 해요?"
**A**: 아무것도 안 해도 됨. 팀원들이 확인하고 병합해줄 때까지 대기.

### Q5: "에러가 나는데 어떻게 고쳐요?"
**A**: 
1. 자기 브랜치에서 코드 수정
2. 커밋 & 푸시
3. PR이 자동으로 업데이트됨
4. 에러 없으면 병합됨

---

## 🎯 요약: 딱 이것만 기억하세요!

### ✅ 해야 할 것
1. **자기 브랜치에서만** 작업
2. 수정하면 **commit & push**
3. master에 합치려면 **PR 만들기**
4. PR 만든 후 **기다리기** (팀원이 확인 후 병합)

### ❌ 하지 말아야 할 것
1. master에 직접 푸시
2. 파일 복사-붙여넁기로 코드 합치기
3. 다른 사람 브랜치 건드리기

---

## 🆘 도움이 필요하면?

1. **HELP_ME.md** 파일 확인 (자동화 스크립트 있음)
2. 팀 채팅방에 물어보기
3. Git 명령어가 헷갈리면 → GitHub Desktop 사용 (GUI 프로그램)

---

## 📚 추가 자료

- [Git 초보자 가이드](https://git-scm.com/book/ko/v2)
- [GitHub Pull Request 튜토리얼](https://docs.github.com/ko/pull-requests)
- [IntelliJ Git 사용법](https://www.jetbrains.com/help/idea/using-git-integration.html)

---

## 🎉 성공 사례

**좋은 예시:**
```bash
# login 담당자의 하루
git checkout login
# 코드 수정...
git add .
git commit -m "feat: 비밀번호 유효성 검사 추가"
git push origin login
gh pr create --base master --head login --fill
# PR 생성 완료! 팀원 리뷰 대기...
```

**나쁜 예시:**
```bash
# ❌ 이러지 마세요!
git checkout master
cp ../write_code/* ./src/  # 다른 브랜치 파일 복사
git add .
git push origin master  # 에러 발생!
```

---

**기억하세요**: Git은 여러분을 도와주는 도구입니다. 겁내지 마세요! 실수해도 되돌릴 수 있습니다. 😊


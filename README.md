# 🎯 감정 일기장 프로젝트

**Java Swing 기반 감정 일기 애플리케이션**

---

## ⚡ 빠른 시작 (팀원용)

### 🚨 처음 보는 분은 여기부터!

**1분 만에 시작하기:**

```bash
# 1. 프로젝트 클론 (처음 한 번만)
git clone https://github.com/EmotionDiarySMU/emotion-diary-project.git
cd emotion-diary-project

# 2. 자기 브랜치로 이동
git checkout login    # 로그인 담당
# git checkout write  # 일기 작성 담당
# git checkout View   # 일기 열람 담당
# git checkout stats  # 통계 담당

# 3. IntelliJ에서 프로젝트 열기
# File → Open → emotion-diary-project 폴더 선택

# 4. 작업 시작!
```

### 📚 필수 문서

**⭐ 반드시 읽어야 할 문서:**
1. **[HELP_ME.md](HELP_ME.md)** ← 빠른 참고 가이드 (3분 완독)
2. **[HOW_TO_COLLABORATE.md](HOW_TO_COLLABORATE.md)** ← 초보자용 완전 가이드 (10분)

**추가 문서:**
- [NEXT_STEPS.md](NEXT_STEPS.md) - 프로젝트 다음 단계
- [create-pr.sh](create-pr.sh) - PR 자동 생성 스크립트
- [update-from-master.sh](update-from-master.sh) - Master 코드 업데이트 스크립트

---

## 🎯 프로젝트 구조

### 브랜치별 담당 기능

| 브랜치 | 담당 기능 | 주요 파일 |
|--------|----------|----------|
| **master** | 전체 통합 | 모든 기능 포함 (배포용) |
| **login** | 로그인/회원가입 | `AuthenticationFrame.java` |
| **write** | 일기 작성 | `WriteDiaryGUI.java` |
| **View** | 일기 열람/수정 | `ViewDiaryPanel.java` |
| **stats** | 통계/차트 | `StatisticsView.java` |

### 폴더 구조

```
src/main/java/com/diary/emotion/
├── login/          # 로그인 기능
├── write/          # 일기 작성 기능
├── view/           # 일기 열람 기능
├── stats/          # 통계 기능
├── db/             # 데이터베이스 (공통)
└── share/          # 공통 유틸리티
```

---

## 💻 개발 환경

- **Java**: 21
- **빌드 도구**: Maven
- **GUI**: Swing
- **데이터베이스**: MySQL
- **의존성**: JFreeChart (통계용)

---

## 🚀 작업 워크플로우

### 일반적인 하루 작업

```bash
# 1. 아침: 최신 코드 받기
./update-from-master.sh

# 2. 코드 수정 (IntelliJ)
# ...작업...

# 3. 저녁: PR 만들기
./create-pr.sh

# 끝!
```

### 규칙

✅ **해야 할 것**
- 자기 브랜치에서만 작업
- 완성되면 PR 생성
- 다른 사람 코드는 master를 통해 받기

❌ **하지 말아야 할 것**
- master에 직접 푸시 (차단됨)
- 파일 복사-붙여넣기로 코드 합치기
- 다른 사람 브랜치 수정

---

## 🔧 유용한 명령어

### 자주 사용하는 Git 명령어

```bash
# 현재 브랜치 확인
git branch

# 자기 브랜치로 이동
git checkout login  # 또는 write, View, stats

# 변경사항 확인
git status

# 최신 코드 받기
git pull origin login

# 커밋 & 푸시
git add .
git commit -m "feat: 기능 설명"
git push origin login
```

### 자동화 스크립트 사용

```bash
# PR 자동 생성
./create-pr.sh

# Master 최신 코드 받기
./update-from-master.sh
```

---

## 📖 Pull Request (PR) 생성하기

### 방법 1: 스크립트 사용 (추천!)

```bash
./create-pr.sh
```

### 방법 2: GitHub 웹사이트

1. 코드 푸시 후 나오는 링크 클릭
2. 제목/설명 입력
3. "Create pull request" 클릭

### 방법 3: GitHub CLI

```bash
gh pr create --base master --head login --fill
```

---

## 🐛 문제 해결

### "master에 푸시가 안 돼요!"
→ 정상입니다! master는 PR로만 수정 가능. 자기 브랜치에서 작업하세요.

### "다른 팀원 코드가 필요해요"
```bash
./update-from-master.sh
```

### "충돌이 났어요!"
1. IntelliJ에서 충돌 파일 열기
2. "Accept Yours" 또는 "Accept Theirs" 선택
3. 저장 후 커밋

자세한 내용은 **[HOW_TO_COLLABORATE.md](HOW_TO_COLLABORATE.md)** 참고

---

## 👥 팀원 정보

각자 담당 브랜치에서 작업해주세요:

- **로그인**: `login` 브랜치
- **일기 작성**: `write` 브랜치
- **일기 열람**: `View` 브랜치
- **통계**: `stats` 브랜치

---

## 📞 도움이 필요하면?

1. **[HELP_ME.md](HELP_ME.md)** 확인
2. **[HOW_TO_COLLABORATE.md](HOW_TO_COLLABORATE.md)** 읽기
3. 팀 채팅방에 질문
4. Google 검색: "git [문제]"

---

## 🎉 시작하기

```bash
# 1. 프로젝트 클론
git clone https://github.com/EmotionDiarySMU/emotion-diary-project.git
cd emotion-diary-project

# 2. 자기 브랜치로 이동
git checkout login  # 자기 담당 브랜치

# 3. IntelliJ에서 열기
# File → Open → emotion-diary-project

# 4. HELP_ME.md 읽기!
cat HELP_ME.md
```

**Happy Coding! 🚀**


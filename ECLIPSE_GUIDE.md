# 🌙 Eclipse 사용자 가이드

**Eclipse & Gemini/ChatGPT 사용자를 위한 특별 가이드**

---

## ⚡ Eclipse에서 시작하기

### 1️⃣ 프로젝트 Import

```
1. Eclipse 실행
2. File → Import → Git → Projects from Git
3. Clone URI 선택 → Next
4. URI: https://github.com/EmotionDiarySMU/emotion-diary-project.git
5. Branch: 자기 담당 브랜치 선택 (login, write, View, stats)
6. Import as general project → Finish
```

### 2️⃣ Maven 프로젝트로 변환

```
1. 프로젝트 우클릭
2. Configure → Convert to Maven Project
3. 자동으로 의존성 다운로드됨
```

### 3️⃣ JDK 21 설정

```
1. 프로젝트 우클릭 → Properties
2. Java Build Path → Libraries
3. JRE System Library 선택 → Edit
4. Installed JREs → Add → Standard VM
5. JDK 21 경로 선택 → Finish
6. JDK 21 선택 → Apply and Close
```

---

## 🔧 Eclipse에서 Git 사용하기

### 브랜치 전환

```
1. 프로젝트 우클릭
2. Team → Switch To → Other
3. Remote Tracking → origin/login 선택 (자기 브랜치)
4. Checkout
```

### 변경사항 커밋 & 푸시

```
1. 프로젝트 우클릭
2. Team → Commit
3. 커밋 메시지 입력: "feat: 기능 설명"
4. 파일 선택
5. Commit and Push 클릭
```

### 최신 코드 받기

```
1. 프로젝트 우클릭
2. Team → Pull
```

---

## 🤖 Gemini/ChatGPT 활용하기

### 코드 작성 시

**질문 형식:**
```
[Gemini/ChatGPT에게]

프로젝트: 감정 일기장 Java Swing 애플리케이션
현재 작업: login 기능 (또는 write, view, stats)
요청: [하고 싶은 것]

예시:
- "로그인 버튼 클릭 시 비밀번호 검증 로직을 추가하고 싶어"
- "일기 작성 후 데이터베이스에 저장하는 코드를 작성해줘"
- "날짜 선택 UI를 만들고 싶어"
```

### Git 명령어 모를 때

**질문 형식:**
```
[Gemini/ChatGPT에게]

"Eclipse에서 [하고 싶은 것]을 어떻게 하나요?"

예시:
- "Eclipse에서 다른 브랜치로 전환하는 방법"
- "Eclipse에서 Git 커밋하고 푸시하는 방법"
- "Eclipse에서 충돌 해결하는 방법"
```

---

## 📋 Eclipse Git 단축키

| 작업 | 단축키/방법 |
|------|-----------|
| **커밋** | 프로젝트 우클릭 → Team → Commit |
| **푸시** | 프로젝트 우클릭 → Team → Push to Upstream |
| **풀** | 프로젝트 우클릭 → Team → Pull |
| **브랜치 전환** | 프로젝트 우클릭 → Team → Switch To |
| **Git History** | 프로젝트 우클릭 → Team → Show in History |

---

## 🚀 자동화 스크립트 사용 (Eclipse에서도 가능!)

Eclipse에서도 터미널을 사용할 수 있습니다:

### Eclipse 내장 터미널 사용

```
1. Window → Show View → Other
2. Terminal → Terminal 검색
3. Open Terminal 클릭
4. 아래 명령어 사용 가능:
   ./create-pr.sh
   ./update-from-master.sh
```

### 외부 터미널 사용

```
1. Eclipse 밖에서 터미널/명령 프롬프트 열기
2. 프로젝트 폴더로 이동:
   cd ~/workspace/emotion-diary-project
3. 스크립트 실행:
   ./create-pr.sh
```

---

## 💡 Eclipse에서 충돌 해결하기

### 충돌 발생 시

```
1. Package Explorer에서 충돌 파일 확인 (빨간 느낌표)
2. 파일 우클릭 → Team → Merge Tool
3. 좌측 (Local): 내 코드
   우측 (Remote): 다른 사람 코드
4. 원하는 것 선택 또는 둘 다 사용
5. Save → 파일 우클릭 → Team → Add to Index
6. Team → Commit
```

---

## 🎯 Gemini/ChatGPT 프롬프트 예시

### 1. 코드 작성 요청

```
[Gemini/ChatGPT에게]

Java Swing으로 로그인 화면을 만들고 있어.
- JTextField로 아이디 입력
- JPasswordField로 비밀번호 입력
- JButton으로 로그인 버튼

로그인 버튼 클릭 시 MySQL 데이터베이스에서 
사용자 정보를 확인하는 코드를 작성해줘.

데이터베이스 연결 코드는 이미 있어:
[DatabaseManager.java 코드 일부 붙여넣기]
```

### 2. 에러 해결

```
[Gemini/ChatGPT에게]

Eclipse에서 다음 에러가 발생했어:
[에러 메시지 복사-붙여넣기]

프로젝트는 Java 21, Maven, Swing을 사용하고 있어.
어떻게 해결하면 될까?
```

### 3. Git 작업

```
[Gemini/ChatGPT에게]

Eclipse에서 Git 사용 중이야.
다른 팀원이 master에 올린 코드를 내 login 브랜치에 
반영하고 싶은데 어떻게 해?

Eclipse GUI로 하는 방법을 단계별로 알려줘.
```

### 4. UI 개선

```
[Gemini/ChatGPT에게]

Java Swing으로 일기 작성 화면을 만들었는데,
디자인을 더 예쁘게 만들고 싶어.

현재 코드:
[WriteDiaryGUI.java 일부 붙여넣기]

색상, 폰트, 레이아웃을 개선하는 코드를 작성해줘.
```

---

## 📝 Eclipse 작업 흐름 (GUI만 사용)

### 아침 작업 시작

```
1. Eclipse 실행
2. 프로젝트 우클릭 → Team → Pull
   (최신 코드 받기)
3. 코드 수정 시작
```

### 작업 완료 후

```
1. 프로젝트 우클릭 → Team → Commit
2. 커밋 메시지: "feat: 로그인 버그 수정"
3. Commit and Push 클릭
4. GitHub 웹사이트 열기
5. "Compare & pull request" 버튼 클릭
6. Create pull request 클릭
```

**또는 터미널 사용:**
```
Window → Show View → Terminal
./create-pr.sh
```

---

## 🔍 Eclipse Git Perspective

### Git Perspective 열기

```
1. Window → Perspective → Open Perspective → Other
2. Git 선택
3. Git Repositories 뷰에서 모든 브랜치 확인 가능
```

### 유용한 뷰

- **Git Repositories**: 브랜치 관리
- **Git Staging**: 커밋할 파일 선택
- **History**: 커밋 히스토리
- **Synchronize**: 원격 저장소와 비교

---

## 💬 자주 묻는 질문 (Eclipse)

### Q: "Maven 의존성이 안 받아져요"
**A**: 
```
1. 프로젝트 우클릭
2. Maven → Update Project
3. Force Update of Snapshots/Releases 체크
4. OK
```

### Q: "JDK 버전 에러가 나요"
**A**: 
```
1. 프로젝트 우클릭 → Properties
2. Java Compiler → 21 선택
3. Java Build Path → Libraries → JDK 21 확인
```

### Q: "Git 메뉴가 안 보여요"
**A**: 
```
1. Help → Install New Software
2. Work with: All Available Sites
3. "EGit" 검색
4. EGit - Git Integration for Eclipse 선택
5. Install
```

### Q: "Gemini가 뭔가요?"
**A**: 
Google의 AI 챗봇입니다.
- 웹사이트: https://gemini.google.com
- 코드 작성, 에러 해결, Git 사용법 등을 물어보세요!

---

## 🎓 Eclipse + AI 활용 팁

### 1. 코드 작성 전에 AI에게 물어보기

```
[Gemini/ChatGPT]
"Java Swing으로 [원하는 기능]을 만들려면 
어떤 컴포넌트를 사용해야 해?"
```

### 2. 에러 메시지 복사해서 물어보기

```
[Gemini/ChatGPT]
[에러 메시지 전체 복사-붙여넣기]
"이 에러가 왜 나는지, 어떻게 고치는지 알려줘"
```

### 3. Git 명령어 모를 때

```
[Gemini/ChatGPT]
"Eclipse에서 [하고 싶은 Git 작업]을 
GUI로 하는 방법을 단계별로 알려줘"
```

---

## 📚 추천 학습 자료

### Eclipse Git 사용법
- Eclipse EGit 공식 문서: https://wiki.eclipse.org/EGit/User_Guide

### Java Swing 튜토리얼
- Oracle 공식 튜토리얼: https://docs.oracle.com/javase/tutorial/uiswing/

### AI 활용
- Gemini: https://gemini.google.com
- ChatGPT: https://chat.openai.com

---

## 🎯 빠른 체크리스트 (Eclipse 사용자)

### 매일 작업 전
- [ ] Eclipse 실행
- [ ] 프로젝트 우클릭 → Team → Pull
- [ ] 자기 브랜치 확인 (Window 하단 상태바)

### 작업 중
- [ ] 코드 작성
- [ ] 자주 저장 (Ctrl+S)
- [ ] 에러 있으면 Gemini/ChatGPT에게 물어보기

### 작업 완료 후
- [ ] 프로젝트 우클릭 → Team → Commit and Push
- [ ] 또는 터미널에서 `./create-pr.sh`
- [ ] GitHub에서 PR 확인

---

## 🎊 결론

**Eclipse 사용자도 동일하게 협업 가능합니다!**

✅ Eclipse GUI로 모든 Git 작업 가능  
✅ Gemini/ChatGPT로 코드 작성 도움 받기  
✅ 자동화 스크립트도 사용 가능  
✅ IntelliJ 사용자와 동일한 워크플로우  

**Eclipse라서 불편한 거 없습니다!** 😊

질문 있으면 Gemini/ChatGPT에게 물어보거나 팀 채팅방에 공유하세요!

---

**Happy Coding with Eclipse! 🌙**


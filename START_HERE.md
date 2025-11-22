# 🎯 감정 일기장 협업 가이드 - 여기서 시작하세요!

**Eclipse 사용자를 위한 완전 가이드 (처음부터 끝까지 모든 것)**

작성일: 2025년 11월 22일  
이 문서 하나만 읽으면 모든 것을 할 수 있습니다!

---

## 📖 목차

1. [빠른 시작 (5분)](#-빠른-시작-5분)
2. [Eclipse 프로젝트 설정](#-eclipse-프로젝트-설정)
3. [Git 사용법 (Eclipse)](#-git-사용법-eclipse)
4. [일상 작업 흐름](#-일상-작업-흐름)
5. [PR 만들기](#-pr-만들기)
6. [문제 해결](#-문제-해결)
7. [Gemini/ChatGPT 활용](#-geminichatgpt-활용)
8. [자주 묻는 질문](#-자주-묻는-질문)

---

## ⚡ 빠른 시작 (5분)

### 🎯 각자 담당 브랜치

| 담당자 | 브랜치명 | 작업 내용 |
|--------|---------|----------|
| 로그인 담당 | `login` | 로그인/회원가입 기능 |
| 일기 작성 담당 | `write` | 일기 작성 기능 |
| 일기 열람 담당 | `View` | 일기 보기/수정/삭제 |
| 통계 담당 | `stats` | 통계/차트 기능 |

**중요:** 자기 브랜치에서만 작업하세요!

### ⚠️ 절대 금지!

❌ master 브랜치에 직접 푸시 (자동 차단됨)  
❌ 다른 사람 브랜치 수정  
❌ 파일 복사-붙여넣기로 코드 합치기  

### ✅ 올바른 방법

```
1. 자기 브랜치에서 작업
2. Eclipse에서 코드 수정
3. 커밋 & 푸시
4. PR(Pull Request) 만들기
5. 팀원 리뷰 후 master에 병합
```

---

## 🌙 Eclipse 프로젝트 설정

### 1단계: 프로젝트 가져오기

```
1. Eclipse 실행

2. File → Import → Git → Projects from Git

3. Clone URI 선택 → Next

4. URI 입력:
   https://github.com/EmotionDiarySMU/emotion-diary-project.git

5. Next 클릭

6. Branch Selection:
   ☑ login    (로그인 담당자)
   ☑ write    (일기 작성 담당자)
   ☑ View     (일기 열람 담당자)
   ☑ stats    (통계 담당자)
   → 자기 브랜치만 체크!

7. Next → Next

8. Import as general project 선택 → Finish
```

### 2단계: Maven 프로젝트로 변환

```
1. Package Explorer에서 프로젝트 우클릭

2. Configure → Convert to Maven Project

3. (자동으로 의존성 다운로드 시작)

4. 완료될 때까지 대기 (1-2분)
```

### 3단계: JDK 21 설정

```
1. 프로젝트 우클릭 → Properties

2. Java Build Path → Libraries 탭

3. JRE System Library 선택 → Edit 버튼

4. Installed JREs 버튼 클릭

5. Add → Standard VM → Next

6. Directory 버튼 클릭 → JDK 21 폴더 선택
   (Mac: /Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home)

7. Finish → JDK 21 체크 → Apply and Close

8. JDK 21 선택 → Finish

9. Apply and Close
```

### 4단계: 자기 브랜치로 전환

```
1. 프로젝트 우클릭

2. Team → Switch To → Other

3. Remote Tracking 펼치기

4. origin/login 선택 (자기 브랜치)

5. Checkout as New Local Branch 선택

6. Finish
```

### ✅ 설정 완료!

화면 하단 상태바에서 현재 브랜치 확인:
```
[emotion-diary-project login]  ← 자기 브랜치명 확인
```

---

## 🔧 Git 사용법 (Eclipse)

### 📥 최신 코드 받기 (Pull)

**매일 아침 작업 시작 전 필수!**

```
방법 1: 메뉴 사용
1. 프로젝트 우클릭
2. Team → Pull
3. 완료!

방법 2: 자동 스크립트
1. 프로젝트 폴더에서 터미널 열기
2. ./update-from-master.sh 실행
```

### 💾 커밋하기 (Commit)

**작업 완료 후 저장**

```
1. 프로젝트 우클릭

2. Team → Commit

3. Commit Message 입력:
   feat: 로그인 버그 수정
   (뭘 했는지 간단히 적기)

4. 변경된 파일 확인:
   - Unstaged Changes에서 파일 선택
   - + 버튼 클릭 (Staged Changes로 이동)

5. Commit and Push 버튼 클릭

6. OK
```

**커밋 메시지 예시:**
```
feat: 로그인 유효성 검사 추가
fix: 비밀번호 입력 버그 수정
docs: 주석 추가
refactor: 코드 정리
```

### ⬆️ 푸시하기 (Push)

**이미 커밋한 내용을 GitHub에 올리기**

```
1. 프로젝트 우클릭

2. Team → Push to Upstream

3. OK

4. Close
```

### 🔄 다른 팀원 코드 가져오기

**다른 사람이 master에 올린 코드를 내 브랜치에 반영**

```
1. 현재 작업 저장:
   - Team → Commit

2. master 최신 코드 받기:
   - 프로젝트 우클릭
   - Team → Fetch from Upstream

3. master를 내 브랜치에 병합:
   - Team → Merge
   - Remote Tracking → origin/master 선택
   - Merge

4. 충돌 있으면 해결 (아래 "충돌 해결" 참고)

5. 완료 후:
   - Team → Push to Upstream
```

**또는 자동 스크립트:**
```
터미널에서:
./update-from-master.sh
```

---

## 📅 일상 작업 흐름

### 아침 (작업 시작)

```
1. Eclipse 실행

2. 최신 코드 받기:
   프로젝트 우클릭 → Team → Pull

3. 자기 브랜치 확인:
   화면 하단에 [login] 또는 [write] 등 표시 확인

4. 작업 시작!
```

### 오후 (코드 작성 중)

```
1. 코드 수정 (자기 담당 기능만)

2. 자주 저장 (Ctrl+S / Cmd+S)

3. 에러 나면?
   → Gemini/ChatGPT에게 에러 메시지 복사해서 물어보기

4. 모르는 거 있으면?
   → Gemini/ChatGPT에게 질문
```

### 저녁 (작업 완료)

```
1. 테스트:
   - 실행해서 에러 없는지 확인
   - 자기 기능 동작하는지 확인

2. 커밋 & 푸시:
   프로젝트 우클릭 → Team → Commit
   → Commit and Push

3. PR 만들기 (아래 "PR 만들기" 참고)

4. 팀 채팅방에 공지:
   "login PR 올렸습니다!"
```

---

## 🚀 PR 만들기

### 방법 1: 자동 스크립트 (추천!)

```
1. Eclipse 안에서:
   Window → Show View → Other → Terminal → Terminal

2. 터미널에서:
   ./create-pr.sh

3. 질문에 답하기:
   - 커밋 메시지: feat: 로그인 기능 개선
   - PR 제목: (Enter = 기본값 사용)
   - PR 설명: (Enter = 기본값 사용)

4. 완료!
```

### 방법 2: GitHub 웹사이트

```
1. 코드 푸시 후 터미널에 나오는 링크 복사:
   remote: Create a pull request for 'login' on GitHub by visiting:
   remote:   https://github.com/EmotionDiarySMU/...

2. 브라우저에서 링크 열기

3. 제목 입력: "feat: 로그인 기능 개선"

4. 설명 입력:
   로그인 버튼 클릭 시 유효성 검사 추가
   - 아이디 빈 값 체크
   - 비밀번호 길이 체크

5. Create pull request 버튼 클릭

6. 완료!
```

### PR 생성 후

```
1. GitHub에서 PR 확인:
   https://github.com/EmotionDiarySMU/emotion-diary-project/pulls

2. 팀원들이 코드 리뷰

3. 승인되면 자동으로 master에 병합

4. 병합 완료 알림 받으면 끝!
```

---

## 🐛 문제 해결

### 충돌(Conflict) 해결하기

**충돌이 뭔가요?**
→ 내 코드와 다른 사람 코드가 같은 부분을 수정했을 때 발생

**Eclipse에서 해결:**

```
1. Package Explorer에서 충돌 파일 확인
   (빨간색 느낌표 표시)

2. 파일 우클릭 → Team → Merge Tool

3. 화면 설명:
   - 좌측 (Local): 내 코드
   - 우측 (Remote): 다른 사람 코드
   - 하단 (Merged): 최종 결과

4. 선택하기:
   - "Copy Current Change from Right" = 다른 사람 코드 사용
   - "Copy Current Change from Left" = 내 코드 사용
   - 직접 수정 = 둘 다 필요하면 하단에서 직접 편집

5. 저장 (Ctrl+S)

6. 파일 우클릭 → Team → Add to Index

7. Team → Commit → "merge: 충돌 해결" 입력

8. Commit and Push
```

### Maven 의존성 에러

```
증상: pom.xml에 빨간 줄, 라이브러리를 못 찾는 에러

해결:
1. 프로젝트 우클릭
2. Maven → Update Project
3. Force Update of Snapshots/Releases 체크
4. OK
5. (1-2분 대기)
```

### JDK 버전 에러

```
증상: "class file has wrong version" 에러

해결:
1. 프로젝트 우클릭 → Properties
2. Java Compiler → Compiler compliance level: 21
3. Apply
4. Java Build Path → Libraries
5. JRE System Library 확인 → JDK 21인지 체크
6. Apply and Close
```

### "master에 푸시가 안 돼요!"

```
증상: ! [remote rejected] master -> master (protected branch hook declined)

→ 정상입니다! master는 보호된 브랜치입니다.
→ PR을 통해서만 master에 코드를 올릴 수 있습니다.

해결:
1. 자기 브랜치로 푸시
2. PR 만들기
3. 승인 후 master에 병합
```

### Git 메뉴가 안 보여요

```
해결:
1. Help → Install New Software
2. Work with: All Available Sites
3. "EGit" 검색
4. EGit - Git Integration for Eclipse 체크
5. Next → Next → I accept → Finish
6. Eclipse 재시작
```

---

## 🤖 Gemini/ChatGPT 활용

### 🌟 Gemini/ChatGPT가 뭔가요?

**AI 챗봇**으로 코드 작성, 에러 해결, Git 사용법 등을 물어볼 수 있습니다.

- **Gemini**: https://gemini.google.com (Google 제공)
- **ChatGPT**: https://chat.openai.com (OpenAI 제공)

### 📝 코드 작성 요청

```
[Gemini/ChatGPT에게 이렇게 질문하세요]

프로젝트: 감정 일기장 Java Swing 애플리케이션
현재 작업: login 기능
사용 기술: Java 21, MySQL, Maven

요청:
JTextField로 아이디를 입력받고
JPasswordField로 비밀번호를 입력받는
로그인 화면을 만들고 싶어.

로그인 버튼 클릭 시 MySQL 데이터베이스에서
사용자 정보를 확인하는 코드를 작성해줘.

현재 DatabaseManager.java로 DB 연결은 되어 있어.
```

**AI가 코드를 작성해주면:**
```
1. 코드 복사
2. Eclipse에 붙여넣기
3. Ctrl+Shift+O (import 자동 추가)
4. 실행해서 테스트
```

### 🐛 에러 해결

```
[Gemini/ChatGPT에게]

Eclipse에서 다음 에러가 발생했어:

[에러 메시지 전체 복사-붙여넣기]
NullPointerException at line 45
...

프로젝트는 Java 21, Maven, MySQL을 사용 중이야.
어떻게 해결하면 될까?
```

### 🔧 Git 사용법 질문

```
[Gemini/ChatGPT에게]

Eclipse에서 Git을 사용하고 있어.
다른 팀원이 master 브랜치에 올린 코드를
내 login 브랜치에 반영하고 싶은데 어떻게 해?

Eclipse GUI로 하는 방법을 단계별로 알려줘.
```

### 🎨 UI 개선

```
[Gemini/ChatGPT에게]

Java Swing으로 로그인 화면을 만들었는데
디자인을 더 예쁘게 만들고 싶어.

현재 코드:
```java
JFrame frame = new JFrame("로그인");
JButton loginBtn = new JButton("로그인");
...
```

색상, 폰트, 레이아웃을 개선하는 코드를 작성해줘.
```

### 💡 AI 활용 팁

1. **구체적으로 질문하기**
   - ❌ "로그인 만들어줘"
   - ✅ "Java Swing으로 JTextField와 JPasswordField를 사용한 로그인 화면을 만들어줘"

2. **에러는 전체 복사**
   - 에러 메시지 전체를 복사해서 붙여넣기
   - "어느 줄에서 에러가 나는지"도 함께 알려주기

3. **현재 코드 보여주기**
   - "지금 이런 코드가 있는데..." 하면서 일부 보여주기
   - AI가 더 정확하게 도와줄 수 있음

4. **단계별로 질문**
   - 한 번에 너무 많은 걸 물어보지 말기
   - 하나씩 해결하면서 진행

---

## ❓ 자주 묻는 질문

### Q1: "어느 브랜치에서 작업해야 하나요?"

**A: 자기 담당 브랜치에서만 작업하세요!**

- 로그인 담당 → `login` 브랜치
- 일기 작성 담당 → `write` 브랜치
- 일기 열람 담당 → `View` 브랜치
- 통계 담당 → `stats` 브랜치

현재 브랜치는 Eclipse 하단 상태바에서 확인!

### Q2: "다른 팀원 코드가 필요한데 어떻게 받나요?"

**A: master를 통해 받으세요!**

```
방법 1: 스크립트
./update-from-master.sh

방법 2: Eclipse
프로젝트 우클릭 → Team → Pull
```

**절대 하지 말 것:**
- 다른 사람 브랜치에서 파일 복사
- 파일을 직접 옮기기

### Q3: "충돌이 났는데 무섭습니다"

**A: 걱정 마세요! Eclipse가 도와줍니다.**

```
1. 충돌 파일 우클릭 → Team → Merge Tool
2. 좌측(내 코드) vs 우측(다른 사람 코드) 비교
3. 필요한 것 선택
4. 저장 → Add to Index → Commit
```

잘 모르겠으면 팀 채팅방에 "충돌 났어요!" 하고 도움 요청!

### Q4: "PR이 정확히 뭔가요?"

**A: "내 코드를 master에 합쳐주세요!" 하고 요청하는 것**

- Pull Request의 약자
- 팀원들이 내 코드를 확인
- 문제 없으면 master에 합쳐짐
- master는 항상 안정적인 상태 유지

### Q5: "실수로 잘못 커밋했어요"

**A: 되돌릴 수 있습니다!**

```
직전 커밋 취소:
1. 프로젝트 우클릭
2. Team → Show in History
3. 이전 커밋 우클릭
4. Reset → Hard
5. (변경사항이 사라지므로 조심!)

아직 푸시 안 했으면:
1. Team → Show in History
2. 이전 커밋 우클릭 → Reset → Mixed
3. 수정 후 다시 커밋
```

### Q6: "Gemini/ChatGPT를 어떻게 활용하나요?"

**A: 코드 작성, 에러 해결, Git 사용법 등 뭐든 물어보세요!**

```
예시:
- "Java Swing으로 버튼 만드는 방법"
- "NullPointerException 에러 해결 방법"
- "Eclipse에서 Git 커밋하는 방법"
- "MySQL 연결 코드 예시"
```

웹사이트:
- Gemini: https://gemini.google.com
- ChatGPT: https://chat.openai.com

### Q7: "자동 스크립트는 어떻게 쓰나요?"

**A: Eclipse 터미널에서 실행!**

```
1. Window → Show View → Other
2. Terminal → Terminal 검색
3. Open Terminal

4. 명령어 입력:
   ./create-pr.sh          (PR 자동 생성)
   ./update-from-master.sh (최신 코드 받기)
```

---

## 📋 체크리스트

### 매일 작업 전
- [ ] Eclipse 실행
- [ ] 프로젝트 우클릭 → Team → Pull
- [ ] 자기 브랜치 확인 (화면 하단)
- [ ] 최신 코드인지 확인

### 작업 중
- [ ] 자기 브랜치에서만 작업
- [ ] 자주 저장 (Ctrl+S)
- [ ] 에러 나면 Gemini/ChatGPT 활용
- [ ] 모르는 거 있으면 팀원에게 질문

### 작업 완료 후
- [ ] 실행해서 테스트
- [ ] 에러 없는지 확인
- [ ] Team → Commit and Push
- [ ] PR 만들기 (./create-pr.sh)
- [ ] 팀 채팅방에 공지

---

## 🎯 요약: 딱 이것만 기억하세요!

### ✅ 해야 할 것
1. **자기 브랜치에서만** 작업
2. **매일 아침** Pull로 최신 코드 받기
3. **작업 완료 후** Commit and Push
4. **master에 합칠 때** PR 만들기
5. **모르는 거** Gemini/ChatGPT에게 질문

### ❌ 하지 말아야 할 것
1. master 브랜치 직접 수정
2. 다른 사람 브랜치 건드리기
3. 파일 복사-붙여넣기로 코드 합치기
4. Pull 없이 작업 시작

---

## 🚀 시작하기

### 1. Eclipse에서 프로젝트 Import
```
File → Import → Git → Projects from Git
→ Clone URI
→ https://github.com/EmotionDiarySMU/emotion-diary-project.git
→ 자기 브랜치 선택
```

### 2. Maven & JDK 21 설정
```
프로젝트 우클릭 → Configure → Convert to Maven Project
프로젝트 우클릭 → Properties → Java Build Path → JDK 21
```

### 3. 자기 브랜치로 전환
```
Team → Switch To → Other → origin/login (자기 브랜치)
```

### 4. 작업 시작!
```
코드 수정 → 저장 → Commit and Push → PR 만들기
```

---

## 📞 도움 요청

### 막혔을 때
1. **Gemini/ChatGPT에게 질문** (https://gemini.google.com)
2. **팀 채팅방에 도움 요청**
3. **GitHub Discussions** (팀원들과 공유)

### 긴급 상황
- 코드가 사라졌어요! → 걱정 마세요, Git에 저장되어 있습니다
- 충돌이 났어요! → Merge Tool로 해결 가능
- 에러가 안 고쳐져요! → 에러 메시지 전체를 Gemini에게 복사

---

## 🎊 마지막 팁

### 성공적인 협업을 위한 습관

1. **매일 아침 Pull 하기**
   - 다른 팀원 변경사항 받기
   - 충돌 최소화

2. **자주 커밋하기**
   - 작은 단위로 커밋
   - 되돌리기 쉬움

3. **명확한 커밋 메시지**
   - "feat: 로그인 유효성 검사 추가"
   - "fix: 비밀번호 입력 버그 수정"

4. **코드 리뷰 적극 참여**
   - 다른 팀원 PR 확인
   - 피드백 주고받기

5. **Gemini/ChatGPT 적극 활용**
   - 막히면 바로 질문
   - 시간 절약

---

## 🎉 끝!

**이 문서 하나로 모든 것을 할 수 있습니다!**

- ✅ Eclipse 프로젝트 설정
- ✅ Git 사용법
- ✅ PR 만들기
- ✅ 충돌 해결
- ✅ Gemini/ChatGPT 활용
- ✅ 문제 해결

**질문 있으면 언제든 팀 채팅방에!**

**Happy Coding! 🚀**

---

**📌 빠른 참고:**
- Gemini: https://gemini.google.com
- ChatGPT: https://chat.openai.com
- 프로젝트: https://github.com/EmotionDiarySMU/emotion-diary-project
- PR 목록: https://github.com/EmotionDiarySMU/emotion-diary-project/pulls


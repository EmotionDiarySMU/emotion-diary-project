# 🆘 도움말 - 빠른 참고용

## 🚀 빠른 시작 (3단계)

### 1️⃣ 코드 수정하기
```bash
git checkout login  # 자기 브랜치 (write, View, stats)
# Eclipse 또는 IntelliJ에서 코드 수정...
```

**Eclipse 사용자?** → **[ECLIPSE_GUIDE.md](ECLIPSE_GUIDE.md)** 먼저 읽으세요!

### 2️⃣ PR 만들기 (자동!)
```bash
./create-pr.sh
```
**끝!** 스크립트가 알아서 커밋, 푸시, PR 생성까지 다 해줍니다.

### 3️⃣ Master 최신 코드 받기
```bash
./update-from-master.sh
```
다른 팀원이 master에 올린 코드를 내 브랜치에 반영합니다.

---

## 📚 자세한 설명

- **ECLIPSE_GUIDE.md** ← Eclipse 사용자 전용 가이드 ⭐ **NEW!**
- **HOW_TO_COLLABORATE.md** ← 초보자용 완전 가이드 (꼭 읽으세요!)
- **NEXT_STEPS.md** ← 프로젝트 다음 단계

---

## 🔧 자주 쓰는 명령어

### 내 브랜치로 이동
```bash
git checkout login    # 로그인 담당
git checkout write    # 일기 작성 담당
git checkout View     # 일기 열람 담당
git checkout stats    # 통계 담당
```

### 변경사항 확인
```bash
git status
```

### 최신 코드 받기
```bash
git pull origin login  # 자기 브랜치명
```

### 커밋 & 푸시 (수동)
```bash
git add .
git commit -m "feat: 무엇을 했는지 적기"
git push origin login  # 자기 브랜치명
```

---

## ⚠️ 문제 해결

### "master에 푸시가 안 돼요!"
✅ 정상입니다! master는 PR로만 수정 가능합니다.
→ 자기 브랜치에서 작업하세요.

### "다른 사람 코드가 필요해요"
```bash
./update-from-master.sh
```

### "충돌(conflict)이 났어요!"
**IntelliJ 사용자:**
1. IntelliJ에서 빨간색 파일 열기
2. "Accept Yours" 또는 "Accept Theirs" 선택
3. 저장 후:
   ```bash
   git add .
   git commit -m "merge: 충돌 해결"
   git push origin login
   ```

**Eclipse 사용자:**
1. 파일 우클릭 → Team → Merge Tool
2. 좌측(Local) 또는 우측(Remote) 선택
3. Save → 파일 우클릭 → Team → Add to Index
4. Team → Commit → Push

### "GitHub CLI가 없어요!"
```bash
brew install gh
gh auth login
```

---

## 💡 팁

### 매일 작업 시작 전
```bash
./update-from-master.sh  # 최신 코드 받기
```

### 작업 완료 후
```bash
./create-pr.sh  # PR 자동 생성
```

### 절대 하지 말 것
- ❌ master 브랜치 건드리기
- ❌ 파일 복사-붙여넣기로 코드 합치기
- ❌ 다른 사람 브랜치 수정하기

---

## 📞 더 도움이 필요하면?

1. **HOW_TO_COLLABORATE.md** 읽기
2. 팀 채팅방에 질문하기
3. Google에 "git [문제]" 검색

---

## ✅ 체크리스트

작업하기 전:
- [ ] 자기 브랜치에 있는지 확인 (`git branch`)
- [ ] 최신 코드 받았는지 확인 (`./update-from-master.sh`)

작업 완료 후:
- [ ] 코드 테스트 (에러 없는지 확인)
- [ ] PR 생성 (`./create-pr.sh`)
- [ ] 팀원들에게 리뷰 요청


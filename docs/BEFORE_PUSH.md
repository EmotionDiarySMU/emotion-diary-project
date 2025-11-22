# ⚠️ Git 푸쉬 전 필수 확인!

## 🔐 비밀번호 보안 체크

**Git에 푸쉬하기 전에 반드시 비밀번호를 원래대로 되돌려야 합니다!**

---

## 📝 되돌려야 할 파일

### 1. DatabaseManager.java

#### ❌ 현재 상태 (실제 비밀번호 - 푸쉬 금지!)
```java
// private static final String DB_PW = "quwrof12"; // 기존 비밀번호 (푸쉬 전 이것으로 되돌릴 것)
private static final String DB_PW = "REMOVED_PASSWORD"; // 실제 사용 비밀번호 (⚠️ 푸쉬 전에 주석 처리 필수!)
```

#### ✅ 푸쉬 전 상태 (예시 비밀번호)
```java
private static final String DB_PW = "quwrof12"; // 비번 (예시)
// private static final String DB_PW = "REMOVED_PASSWORD"; // 실제 비밀번호는 로컬에서만 사용
```

---

### 2. Main.java

#### ❌ 현재 상태
```java
System.err.println("      현재 설정: DB_ID=root, DB_PW=REMOVED_PASSWORD");
```

#### ✅ 푸쉬 전 상태
```java
System.err.println("      현재 설정: DB_ID=root, DB_PW=본인의_비밀번호");
```

---

## 🚀 푸쉬 전 체크리스트

### Git 푸쉬하기 전:

- [ ] DatabaseManager.java의 DB_PW를 예시 비밀번호로 변경
- [ ] 실제 비밀번호 줄은 주석 처리
- [ ] Main.java의 에러 메시지에서 비밀번호 제거
- [ ] git status로 변경 사항 확인
- [ ] 이 파일(BEFORE_PUSH.md)도 삭제 또는 .gitignore 추가

### 자동 되돌리기 (빠른 방법)

#### 방법 1: 직접 수정
```bash
# DatabaseManager.java 열고
# 17번째 줄 주석 해제, 18번째 줄 주석 처리

# Main.java 열고
# "DB_PW=REMOVED_PASSWORD" → "DB_PW=본인의_비밀번호"로 변경
```

#### 방법 2: 스크립트 사용
아래 명령어를 실행하면 자동으로 되돌립니다:

```bash
# DatabaseManager.java 되돌리기
sed -i '' 's/\/\/ private static final String DB_PW = "quwrof12";/private static final String DB_PW = "quwrof12"; \/\/ 비번 (예시)/' src/main/java/com/diary/emotion/DatabaseManager.java
sed -i '' 's/private static final String DB_PW = "U9Bsi7sj1\*";/\/\/ private static final String DB_PW = "REMOVED_PASSWORD"; \/\/ 실제 비밀번호 (로컬 전용)/' src/main/java/com/diary/emotion/DatabaseManager.java

# Main.java 되돌리기
sed -i '' 's/DB_PW=U9Bsi7sj1\*/DB_PW=본인의_비밀번호/' src/main/java/com/diary/emotion/Main.java
```

---

## 🔒 .gitignore 추가 (권장)

비밀번호 파일을 아예 Git에서 제외하는 방법:

### 1. 로컬 설정 파일 생성
```bash
# db.properties 파일 생성
cat > db.properties << EOF
db.url=jdbc:mysql://localhost:3306/emotion_diary?serverTimezone=UTC
db.username=root
db.password=REMOVED_PASSWORD
EOF
```

### 2. .gitignore에 추가
```
db.properties
```

### 3. 코드에서 읽기
```java
Properties props = new Properties();
try (FileInputStream fis = new FileInputStream("db.properties")) {
    props.load(fis);
    String password = props.getProperty("db.password");
}
```

---

## ⚠️ 이미 푸쉬했다면?

만약 실수로 비밀번호를 푸쉬했다면:

### 1. 즉시 비밀번호 변경
```sql
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY '새로운_비밀번호';
FLUSH PRIVILEGES;
```

### 2. Git 히스토리에서 제거
```bash
# BFG Repo-Cleaner 사용 (권장)
# 또는 git filter-branch 사용
```

### 3. 강제 푸쉬
```bash
git push --force
```

---

## 💡 현재 상태

### ✅ 로컬에서 작동 중
- DatabaseManager.java: 실제 비밀번호 사용 중
- Main.java: 실제 비밀번호 표시 중
- **이 상태로 푸쉬 금지!**

### 🎯 푸쉬 전 목표
- DatabaseManager.java: 예시 ��밀번호 + 주석으로 안내
- Main.java: 비밀번호 숨김
- **안전하게 푸쉬 가능**

---

## 📋 요약

**지금 상태:**
```
⚠️  실제 비밀번호가 코드에 있음 (REMOVED_PASSWORD)
❌ Git 푸쉬 금지!
✅ 로컬에서는 정상 작동
```

**푸쉬하려면:**
1. DatabaseManager.java 되돌리기
2. Main.java 되돌리기
3. git status 확인
4. 안전하게 푸쉬! ✅

---

**이 파일은 푸쉬하지 마세요!**
`.gitignore`에 `BEFORE_PUSH.md` 추가를 권장합니다.


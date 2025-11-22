#!/bin/bash

echo "=========================================="
echo "🔍 데이터베이스 연결 문제 진단"
echo "=========================================="
echo ""

# 1. MySQL 프로세스 확인
echo "1️⃣ MySQL 서버 실행 상태 확인..."
if pgrep -x "mysqld" > /dev/null 2>&1; then
    echo "   ✅ MySQL 서버가 실행 중입니다"
else
    echo "   ❌ MySQL 서버가 실행되지 않고 있습니다"
    echo ""
    echo "   해결 방법:"
    echo "   - macOS: brew services start mysql"
    echo "   - 또는: mysql.server start"
    exit 1
fi

echo ""

# 2. MySQL 포트 확인
echo "2️⃣ MySQL 포트(3306) 확인..."
if lsof -i :3306 > /dev/null 2>&1; then
    echo "   ✅ MySQL이 포트 3306에서 실행 중입니다"
    echo "   실행 정보:"
    lsof -i :3306 | grep LISTEN
else
    echo "   ❌ 포트 3306에서 MySQL이 실행되지 않습니다"
    echo ""
    echo "   해결 방법:"
    echo "   - MySQL 설정 파일에서 포트 확인"
    echo "   - 또는: sudo lsof -i :3306 (관리자 권한으로 확인)"
fi

echo ""

# 3. MySQL 연결 테스트
echo "3️⃣ MySQL 연결 테스트..."
echo "   (비밀번호를 입력해주세요)"
echo ""

if mysql -u root -p -e "SELECT 'MySQL 연결 성공!' AS status;" 2>/dev/null; then
    echo ""
    echo "   ✅ MySQL 연결 성공!"
else
    echo ""
    echo "   ❌ MySQL 연결 실패"
    echo ""
    echo "   해결 방법:"
    echo "   1. 비밀번호가 틀렸을 수 있습니다"
    echo "   2. DatabaseManager.java의 DB_PW를 확인하세요"
    echo "      현재 설정: DB_PW = \"quwrof12\""
    echo ""
    echo "   비밀번호 재설정 방법:"
    echo "   - mysql -u root -p"
    echo "   - ALTER USER 'root'@'localhost' IDENTIFIED BY '새비밀번호';"
fi

echo ""

# 4. emotion_diary 데이터베이스 확인
echo "4️⃣ emotion_diary 데이터베이스 확인..."
echo "   (다시 비밀번호를 입력해주세요)"
echo ""

DB_EXISTS=$(mysql -u root -p -e "SHOW DATABASES LIKE 'emotion_diary';" 2>/dev/null | grep emotion_diary)

if [ -n "$DB_EXISTS" ]; then
    echo "   ✅ emotion_diary 데이터베이스가 존재합니다"
    echo ""
    echo "   테이블 목록:"
    mysql -u root -p -e "USE emotion_diary; SHOW TABLES;" 2>/dev/null
else
    echo "   ℹ️  emotion_diary 데이터베이스가 아직 생성되지 않았습니다"
    echo "      Main.java를 실행하면 자동으로 생성됩니다"
fi

echo ""
echo "=========================================="
echo "📋 진단 완료"
echo "=========================================="
echo ""
echo "💡 다음 단계:"
echo "   1. 위의 모든 항목이 ✅ 이면 Main.java를 실행하세요"
echo "   2. ❌ 항목이 있다면 해당 해결 방법을 따라주세요"
echo "   3. 문제가 계속되면 에러 메시지를 확인하세요"
echo ""


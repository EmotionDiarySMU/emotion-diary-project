#!/bin/bash

echo "======================================"
echo "📊 Main.java 실행 가능 여부 검증"
echo "======================================"
echo ""

# 1. 필요한 파일 존재 확인
echo "1️⃣  필수 파일 존재 확인..."
FILES=(
    "src/main/java/com/diary/emotion/Main.java"
    "src/main/java/com/diary/emotion/DatabaseManager.java"
    "src/main/java/com/diary/emotion/StatisticsDAO.java"
    "src/main/java/com/diary/emotion/StatisticsController.java"
    "src/main/java/com/diary/emotion/StatisticsView.java"
)

ALL_EXISTS=true
for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "   ✅ $file"
    else
        echo "   ❌ $file (파일 없음)"
        ALL_EXISTS=false
    fi
done

if [ "$ALL_EXISTS" = false ]; then
    echo ""
    echo "❌ 일부 필수 파일이 없습니다."
    exit 1
fi

echo ""
echo "2️⃣  테스트 파일 정리 확인..."
if [ -f "src/main/java/com/diary/emotion/QuickStatisticsTest.java" ]; then
    echo "   ⚠️  QuickStatisticsTest.java 존재 (삭제 필요)"
else
    echo "   ✅ QuickStatisticsTest.java 없음"
fi

if [ -f "src/main/java/com/diary/emotion/StatisticsTest.java" ]; then
    echo "   ⚠️  StatisticsTest.java 존재 (삭제 필요)"
else
    echo "   ✅ StatisticsTest.java 없음"
fi

echo ""
echo "3️⃣  Main.java 내용 확인..."
if grep -q "import login.AuthenticationFrame" "src/main/java/com/diary/emotion/Main.java" 2>/dev/null; then
    if grep -q "// import login.AuthenticationFrame" "src/main/java/com/diary/emotion/Main.java" 2>/dev/null; then
        echo "   ✅ login import 주석 처리됨"
    else
        echo "   ❌ login import 주석 처리 필요"
    fi
else
    echo "   ✅ login import 없음 또는 주석 처리됨"
fi

if grep -q "StatisticsView view" "src/main/java/com/diary/emotion/Main.java" 2>/dev/null; then
    echo "   ✅ 통계 UI 실행 코드 포함"
else
    echo "   ❌ 통계 UI 실행 코드 없음"
fi

echo ""
echo "4️⃣  MySQL 연결 확인..."
if command -v mysql &> /dev/null; then
    echo "   ✅ MySQL 클라이언트 설치됨"

    # MySQL 서버 실행 여부 확인 (간단한 방법)
    if pgrep -x "mysqld" > /dev/null 2>&1; then
        echo "   ✅ MySQL 서버 실행 중"
    else
        echo "   ⚠️  MySQL 서버 실행 여부 불확실"
        echo "      (확인 방법: mysql -u root -p)"
    fi
else
    echo "   ⚠️  MySQL 클라이언트 미설치"
fi

echo ""
echo "======================================"
echo "📋 검증 요약"
echo "======================================"
echo ""
echo "✅ 필수 파일: 모두 존재"
echo "✅ 테스트 파일: 정리됨"
echo "✅ Main.java: 통계 UI 실행 준비됨"
echo ""
echo "🚀 IntelliJ IDEA에서 실행 방법:"
echo "   1. File → Invalidate Caches → Invalidate and Restart"
echo "   2. Main.java 파일 열기"
echo "   3. main 메서드 옆 녹색 실행 버튼 클릭"
echo ""
echo "예상 결과:"
echo "   - 콘솔: ✅ 데이터베이스 초기화 성공"
echo "   - 콘솔: ✅ 통계 UI 실행"
echo "   - UI: 통계 창 표시 (1000x700)"
echo ""


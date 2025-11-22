#!/bin/bash

echo "=========================================="
echo "BFG를 사용한 비밀번호 제거 (간단한 방법)"
echo "=========================================="
echo ""

# 1. BFG 설치 확인
if ! command -v bfg &> /dev/null; then
    echo "BFG Repo-Cleaner를 설치합니다..."
    brew install bfg
fi

echo "✅ BFG 준비 완료"
echo ""

# 2. 백업 생성
BACKUP_BRANCH="backup-bfg-$(date +%Y%m%d-%H%M%S)"
git branch "$BACKUP_BRANCH"
echo "✅ 백업 브랜치 생성: $BACKUP_BRANCH"
echo ""

# 3. 교체할 텍스트 파일 생성
cat > /tmp/passwords.txt << 'EOF'
REMOVED_PASSWORD
EOF

echo "=========================================="
echo "비밀번호 교체 실행"
echo "=========================================="
echo ""
echo "다음 텍스트를 'REMOVED'로 교체합니다:"
cat /tmp/passwords.txt
echo ""

# 4. BFG 실행
bfg --replace-text /tmp/passwords.txt .

echo ""
echo "=========================================="
echo "Git 정리"
echo "=========================================="

# 5. Git reflog 만료 및 gc
git reflog expire --expire=now --all
git gc --prune=now --aggressive

echo "✅ 완료!"
echo ""
echo "=========================================="
echo "검증"
echo "=========================================="

# 6. 검증
if git log --all -p | grep -q "U9Bsi7sj1"; then
    echo "⚠️  비밀번호가 아직 히스토리에 남아있습니다."
else
    echo "✅ 비밀번호가 히스토리에서 제거되었습니다!"
fi

echo ""
echo "=========================================="
echo "다음 단계"
echo "=========================================="
echo ""
echo "1. 변경 사항 확인:"
echo "   git log --all -p | grep 'REMOVED'"
echo ""
echo "2. 원격 저장소에 force push:"
echo "   git push origin stats --force"
echo ""
echo "3. 백업 브랜치 삭제 (성공 후):"
echo "   git branch -D $BACKUP_BRANCH"
echo ""


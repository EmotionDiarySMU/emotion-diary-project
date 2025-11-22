#!/bin/bash

echo "=========================================="
echo "Git 히스토리에서 비밀번호 제거"
echo "=========================================="
echo ""
echo "⚠️  경고: 이 스크립트는 Git 히스토리를 수정합니다!"
echo "⚠️  원격 저장소에 이미 푸쉬된 경우, force push가 필요합니다!"
echo ""
read -p "계속하시겠습니까? (y/n): " confirm

if [ "$confirm" != "y" ]; then
    echo "취소되었습니다."
    exit 0
fi

echo ""
echo "=========================================="
echo "1단계: 현재 상태 백업"
echo "=========================================="

# 현재 브랜치 확인
CURRENT_BRANCH=$(git branch --show-current)
echo "현재 브랜치: $CURRENT_BRANCH"

# 백업 브랜치 생성
BACKUP_BRANCH="backup-$(date +%Y%m%d-%H%M%S)"
git branch "$BACKUP_BRANCH"
echo "✅ 백업 브랜치 생성: $BACKUP_BRANCH"

echo ""
echo "=========================================="
echo "2단계: git-filter-repo 설치 확인"
echo "=========================================="

# git-filter-repo가 있는지 확인
if ! command -v git-filter-repo &> /dev/null; then
    echo "git-filter-repo가 설치되어 있지 않습니다."
    echo ""
    echo "설치 방법:"
    echo "  brew install git-filter-repo"
    echo ""
    read -p "지금 설치하시겠습니까? (y/n): " install_confirm

    if [ "$install_confirm" = "y" ]; then
        brew install git-filter-repo
    else
        echo ""
        echo "수동 설치 후 다시 실행해주세요."
        exit 1
    fi
fi

echo "✅ git-filter-repo 확인됨"

echo ""
echo "=========================================="
echo "3단계: 비밀번호 교체 스크립트 생성"
echo "=========================================="

# 임시 스크립트 파일 생성
cat > /tmp/replace_password.py << 'EOF'
#!/usr/bin/env python3
import re
import sys

# 읽기
old_content = sys.stdin.buffer.read()
content = old_content.decode('utf-8', errors='ignore')

# 비밀번호 패턴 교체
replacements = [
    # DatabaseManager.java의 비밀번호
    (r'private static final String DB_PW = "U9Bsi7sj1\*";',
     'private static final String DB_PW = "your_password_here";'),

    # Main.java의 에러 메시지
    (r'DB_PW=U9Bsi7sj1\*',
     'DB_PW=your_password_here'),

    # 실제 비밀번호가 포함된 모든 경우
    (r'U9Bsi7sj1\*',
     'your_password_here'),
]

for pattern, replacement in replacements:
    content = re.sub(pattern, replacement, content)

# 쓰기
sys.stdout.buffer.write(content.encode('utf-8'))
EOF

chmod +x /tmp/replace_password.py

echo "✅ 비밀번호 교체 스크립트 생성 완료"

echo ""
echo "=========================================="
echo "4단계: Git 히스토리 재작성"
echo "=========================================="

echo "⏳ 모든 커밋의 파일을 검사하고 비밀번호를 교체합니다..."
echo "   (시간이 걸릴 수 있습니다)"

git filter-repo --force \
    --blob-callback '
import re

# Java 파일만 처리
if blob.original_id:
    content = blob.data.decode("utf-8", errors="ignore")

    # 비밀번호 패턴 교체
    content = re.sub(r"U9Bsi7sj1\*", "your_password_here", content)

    blob.data = content.encode("utf-8")
' 2>&1 | tee /tmp/filter-repo.log

if [ $? -eq 0 ]; then
    echo "✅ Git 히스토리 재작성 완료!"
else
    echo "❌ 오류가 발생했습니다. 로그를 확인하세요: /tmp/filter-repo.log"
    echo ""
    echo "백업 브랜치로 복구하려면:"
    echo "  git checkout $BACKUP_BRANCH"
    exit 1
fi

echo ""
echo "=========================================="
echo "5단계: 검증"
echo "=========================================="

echo "비밀번호가 남아있는지 확인 중..."

# 모든 커밋에서 비밀번호 검색
if git log --all -p | grep -q "U9Bsi7sj1"; then
    echo "⚠️  경고: 아직 비밀번호가 히스토리에 남아있습니다!"
    echo ""
    echo "수동 확인:"
    echo "  git log --all -p | grep 'U9Bsi7sj1'"
else
    echo "✅ 히스토리에서 비밀번호가 제거되었습니다!"
fi

# 현재 작업 파일 확인
if grep -r "U9Bsi7sj1" src/ 2>/dev/null; then
    echo ""
    echo "⚠️  현재 작업 파일에는 여전히 비밀번호가 있습니다."
    echo "   (이것은 정상입니다 - 로컬에서만 사용)"
else
    echo "✅ 현재 작업 파일에서도 비밀번호가 제거되었습니다."
fi

echo ""
echo "=========================================="
echo "6단계: 원격 저장소 업데이트"
echo "=========================================="

echo ""
echo "⚠️  중요: 원격 저장소에 이미 푸쉬했다면 force push가 필요합니다!"
echo ""
echo "Force push 명령어:"
echo "  git push origin $CURRENT_BRANCH --force"
echo ""
echo "⚠️  주의사항:"
echo "  1. 팀원과 협업 중이라면 먼저 알려주세요!"
echo "  2. 다른 사람이 이미 pull 받았다면 재동기화가 필요합니다."
echo "  3. force push는 원격 히스토리를 완전히 덮어씁니다."
echo ""
read -p "지금 force push를 실행하시겠습니까? (y/n): " push_confirm

if [ "$push_confirm" = "y" ]; then
    echo ""
    echo "원격 저장소에 force push 중..."
    git push origin "$CURRENT_BRANCH" --force

    if [ $? -eq 0 ]; then
        echo "✅ 원격 저장소 업데이트 완료!"
    else
        echo "❌ Push 실패. 수동으로 실행해주세요:"
        echo "   git push origin $CURRENT_BRANCH --force"
    fi
else
    echo "나중에 수동으로 실행하세요:"
    echo "  git push origin $CURRENT_BRANCH --force"
fi

echo ""
echo "=========================================="
echo "완료!"
echo "=========================================="
echo ""
echo "✅ 작업 완료 요약:"
echo "  - 백업 브랜치: $BACKUP_BRANCH"
echo "  - 현재 브랜치: $CURRENT_BRANCH"
echo "  - 히스토리 재작성: 완료"
echo ""
echo "💡 다음 단계:"
echo "  1. 비밀번호가 완전히 제거되었는지 확인:"
echo "     git log --all -p | grep 'U9Bsi7sj1'"
echo ""
echo "  2. 현재 작업 파일의 비밀번호 복원 (로컬 사용):"
echo "     실제 비밀번호를 다시 설정하세요."
echo ""
echo "  3. 문제가 있다면 백업에서 복구:"
echo "     git checkout $BACKUP_BRANCH"
echo ""
echo "  4. 성공했다면 백업 브랜치 삭제:"
echo "     git branch -D $BACKUP_BRANCH"
echo ""


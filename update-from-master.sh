#!/bin/bash
# 🔄 Master 최신 코드 가져오기 스크립트
# 사용법: ./update-from-master.sh

set -e

echo "=========================================="
echo "  🔄 Master 최신 코드 반영하기"
echo "=========================================="
echo ""

# 현재 브랜치 확인
CURRENT_BRANCH=$(git branch --show-current)

if [ "$CURRENT_BRANCH" = "master" ]; then
    echo "❌ 에러: master 브랜치에 있습니다!"
    echo "먼저 자기 브랜치로 이동하세요:"
    echo "  git checkout login"
    exit 1
fi

echo "✅ 현재 브랜치: $CURRENT_BRANCH"
echo ""

# 변경사항 확인
if ! git diff-index --quiet HEAD --; then
    echo "⚠️  저장하지 않은 변경사항이 있습니다!"
    echo ""
    git status --short
    echo ""
    read -p "먼저 커밋하시겠습니까? (y/n): " commit_now

    if [ "$commit_now" = "y" ]; then
        read -p "커밋 메시지: " commit_msg
        git add .
        git commit -m "$commit_msg"
        echo "✅ 커밋 완료!"
    else
        echo ""
        echo "변경사항을 임시 저장합니다..."
        git stash
        STASHED=1
        echo "✅ 임시 저장 완료!"
    fi
fi

echo ""
echo "📥 Master 브랜치 최신 코드 다운로드 중..."
git fetch origin master

echo ""
echo "🔄 Master 코드를 현재 브랜치에 병합 중..."

# master 코드 병합
if git merge origin/master --no-edit; then
    echo "✅ 병합 완료!"

    # stash한 내용 복원
    if [ "$STASHED" = "1" ]; then
        echo ""
        echo "📦 임시 저장한 내용 복원 중..."
        git stash pop
        echo "✅ 복원 완료!"
    fi

    echo ""
    echo "=========================================="
    echo "  ✅ 업데이트 완료!"
    echo "=========================================="
    echo ""
    echo "다음 단계:"
    echo "1. IntelliJ에서 코드 확인"
    echo "2. 에러가 있으면 수정"
    echo "3. 테스트 실행"
    echo "4. 문제 없으면 push:"
    echo "   git push origin $CURRENT_BRANCH"
    echo ""
else
    echo ""
    echo "⚠️  충돌이 발생했습니다!"
    echo ""
    echo "다음 단계:"
    echo "1. IntelliJ에서 충돌 파일 확인 (빨간색 표시)"
    echo "2. 각 파일 열어서 충돌 해결"
    echo "   - Accept Yours: 내 코드 유지"
    echo "   - Accept Theirs: master 코드 사용"
    echo "   - Merge: 둘 다 사용"
    echo "3. 충돌 해결 후:"
    echo "   git add ."
    echo "   git commit -m 'merge: master 최신 코드 반영'"
    echo "   git push origin $CURRENT_BRANCH"
    echo ""
    echo "충돌 파일 목록:"
    git diff --name-only --diff-filter=U
    echo ""

    # stash 복원 안내
    if [ "$STASHED" = "1" ]; then
        echo "⚠️  주의: 충돌 해결 후 다음 명령어로 임시 저장 내용 복원:"
        echo "   git stash pop"
        echo ""
    fi
fi


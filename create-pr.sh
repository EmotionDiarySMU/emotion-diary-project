#!/bin/bash
# 🚀 자동 PR 생성 스크립트
# 사용법: ./create-pr.sh

set -e  # 에러 발생 시 중단

echo "=========================================="
echo "  🎯 감정 일기장 PR 자동 생성 도구"
echo "=========================================="
echo ""

# 현재 브랜치 확인
CURRENT_BRANCH=$(git branch --show-current)

# master 브랜치인지 확인
if [ "$CURRENT_BRANCH" = "master" ]; then
    echo "❌ 에러: master 브랜치에서는 작업할 수 없습니다!"
    echo ""
    echo "다음 명령어로 자기 브랜치로 이동하세요:"
    echo "  git checkout login    # 로그인 담당"
    echo "  git checkout write    # 일기 작성 담당"
    echo "  git checkout View     # 일기 열람 담당"
    echo "  git checkout stats    # 통계 담당"
    exit 1
fi

echo "✅ 현재 브랜치: $CURRENT_BRANCH"
echo ""

# 변경사항 확인
if git diff-index --quiet HEAD --; then
    echo "ℹ️  변경사항이 없습니다."
    echo ""
    read -p "그래도 계속하시겠습니까? (y/n): " continue
    if [ "$continue" != "y" ]; then
        echo "취소되었습니다."
        exit 0
    fi
else
    echo "📝 변경된 파일:"
    git status --short
    echo ""

    # 커밋 메시지 입력
    read -p "커밋 메시지를 입력하세요 (예: feat: 로그인 버그 수정): " commit_msg

    if [ -z "$commit_msg" ]; then
        echo "❌ 커밋 메시지가 비어있습니다!"
        exit 1
    fi

    # 변경사항 커밋
    echo ""
    echo "📦 변경사항을 커밋 중..."
    git add .
    git commit -m "$commit_msg"
    echo "✅ 커밋 완료!"
fi

# 원격 저장소에 푸시
echo ""
echo "⬆️  GitHub에 업로드 중..."
git push origin "$CURRENT_BRANCH"
echo "✅ 업로드 완료!"

# PR 제목 입력
echo ""
read -p "PR 제목을 입력하세요 (Enter = 기본값 사용): " pr_title

if [ -z "$pr_title" ]; then
    case "$CURRENT_BRANCH" in
        login)
            pr_title="feat: 로그인 기능 개선"
            ;;
        write)
            pr_title="feat: 일기 작성 기능 개선"
            ;;
        View)
            pr_title="feat: 일기 열람 기능 개선"
            ;;
        stats)
            pr_title="feat: 통계 기능 개선"
            ;;
        *)
            pr_title="feat: $CURRENT_BRANCH 기능 개선"
            ;;
    esac
fi

# PR 설명 입력
echo ""
read -p "PR 설명을 입력하세요 (Enter = 기본값 사용): " pr_body

if [ -z "$pr_body" ]; then
    pr_body="$CURRENT_BRANCH 브랜치의 최신 변경사항을 master에 반영합니다."
fi

# GitHub CLI 확인
if ! command -v gh &> /dev/null; then
    echo ""
    echo "⚠️  GitHub CLI(gh)가 설치되어 있지 않습니다."
    echo ""
    echo "다음 중 하나를 선택하세요:"
    echo ""
    echo "1. GitHub CLI 설치 (권장):"
    echo "   brew install gh"
    echo "   gh auth login"
    echo ""
    echo "2. 웹에서 직접 PR 생성:"
    echo "   https://github.com/EmotionDiarySMU/emotion-diary-project/compare/master...$CURRENT_BRANCH"
    echo ""
    exit 0
fi

# PR 생성
echo ""
echo "🔄 Pull Request 생성 중..."
PR_URL=$(gh pr create --base master --head "$CURRENT_BRANCH" --title "$pr_title" --body "$pr_body" 2>&1)

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  ✅ PR 생성 완료!"
    echo "=========================================="
    echo ""
    echo "$PR_URL"
    echo ""
    echo "다음 단계:"
    echo "1. 위 링크를 브라우저에서 열기"
    echo "2. 팀원들에게 리뷰 요청"
    echo "3. 승인되면 master에 자동 병합"
    echo ""
else
    echo ""
    echo "⚠️  PR 생성 중 문제가 발생했습니다."
    echo ""
    if echo "$PR_URL" | grep -q "already exists"; then
        echo "ℹ️  이미 PR이 존재합니다. GitHub에서 확인하세요:"
        echo "   https://github.com/EmotionDiarySMU/emotion-diary-project/pulls"
    else
        echo "수동으로 PR을 생성하세요:"
        echo "https://github.com/EmotionDiarySMU/emotion-diary-project/compare/master...$CURRENT_BRANCH"
    fi
    echo ""
fi


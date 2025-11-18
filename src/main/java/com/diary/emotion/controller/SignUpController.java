package com.diary.emotion.controller;

import com.diary.emotion.view.SignUpView;
import com.diary.emotion.model.UserDAO;
import javax.swing.JOptionPane;

/**
 * 회원가입 Controller 클래스
 * SignUpView와 UserDAO를 연결하여 회원가입 로직을 처리합니다.
 *
 * 주요 기능:
 * - 회원가입 버튼 클릭 처리
 * - 입력값 검증 (아이디 길이, 비밀번호 일치 등)
 * - 아이디 중복 확인
 * - 회원가입 성공 시 로그인 화면으로 전환
 */
public class SignUpController {

    private SignUpView view;
    private UserDAO dao;
    private Runnable onSignUpSuccess;
    private Runnable onBackRequest;

    /**
     * SignUpController 생성자
     * View와 DAO를 연결하고 이벤트 리스너를 설정합니다.
     *
     * @param view SignUpView 객체
     * @param dao UserDAO 객체
     */
    public SignUpController(SignUpView view, UserDAO dao) {
        this.view = view;
        this.dao = dao;

        addListeners();
    }

    /**
     * View의 버튼들에 이벤트 리스너를 추가합니다.
     */
    private void addListeners() {
        // 회원가입 버튼 클릭 시
        view.getSignUpButton().addActionListener(e -> handleSignUp());

        // 뒤로가기 버튼 클릭 시
        view.getBackButton().addActionListener(e -> handleBackRequest());
    }

    /**
     * 회원가입 버튼 클릭 시 호출되는 메소드
     * 입력값을 검증하고 DB에 신규 사용자를 등록합니다.
     */
    private void handleSignUp() {
        String userId = view.getUserId();
        String password = view.getPassword();
        String passwordConfirm = view.getPasswordConfirm();

        // 1. 입력값 검증: 빈 필드 확인
        if (userId.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "모든 필드를 입력해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. 입력값 검증: 아이디 길이 (3~20자)
        if (userId.length() < 3 || userId.length() > 20) {
            JOptionPane.showMessageDialog(view,
                "아이디는 3~20자 사이여야 합니다.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. 입력값 검증: 비밀번호 길이 (4~20자)
        if (password.length() < 4 || password.length() > 20) {
            JOptionPane.showMessageDialog(view,
                "비밀번호는 4~20자 사이여야 합니다.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. 입력값 검증: 비밀번호 일치 확인
        if (!password.equals(passwordConfirm)) {
            JOptionPane.showMessageDialog(view,
                "비밀번호가 일치하지 않습니다.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            view.clearFields();
            view.focusUserId();
            return;
        }

        // 5. 아이디 중복 확인
        if (dao.userExists(userId)) {
            JOptionPane.showMessageDialog(view,
                "이미 사용 중인 아이디입니다.",
                "회원가입 실패",
                JOptionPane.ERROR_MESSAGE);
            view.clearFields();
            view.focusUserId();
            return;
        }

        // 6. 회원가입 처리
        boolean success = dao.registerUser(userId, password);

        if (success) {
            JOptionPane.showMessageDialog(view,
                "회원가입이 완료되었습니다!\n로그인 화면으로 이동합니다.",
                "회원가입 성공",
                JOptionPane.INFORMATION_MESSAGE);

            view.clearFields();

            // 7. 로그인 화면으로 전환 (콜백 실행)
            if (onSignUpSuccess != null) {
                onSignUpSuccess.run();
            }
        } else {
            JOptionPane.showMessageDialog(view,
                "회원가입 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.",
                "회원가입 실패",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 뒤로가기 버튼 클릭 시 호출되는 메소드
     * 로그인 화면으로 돌아갑니다.
     */
    private void handleBackRequest() {
        view.clearFields();

        if (onBackRequest != null) {
            onBackRequest.run();
        }
    }

    /**
     * 회원가입 성공 시 실행할 콜백을 설정합니다.
     *
     * @param callback 실행할 콜백 (Runnable)
     */
    public void setOnSignUpSuccess(Runnable callback) {
        this.onSignUpSuccess = callback;
    }

    /**
     * 뒤로가기 버튼 클릭 시 실행할 콜백을 설정합니다.
     *
     * @param callback 실행할 콜백 (Runnable)
     */
    public void setOnBackRequest(Runnable callback) {
        this.onBackRequest = callback;
    }
}


package com.diary.emotion.controller;

import com.diary.emotion.view.LoginView;
import com.diary.emotion.model.UserDAO;
import com.diary.emotion.util.Session;
import javax.swing.JOptionPane;

/**
 * 로그인 Controller 클래스
 * LoginView와 UserDAO를 연결하여 로그인 로직을 처리합니다.
 *
 * 주요 기능:
 * - 로그인 버튼 클릭 처리
 * - 입력값 검증
 * - 로그인 성공 시 세션 설정 및 메인 화면 전환
 * - 회원가입 화면 전환
 */
public class LoginController {

    private LoginView view;
    private UserDAO dao;
    private Runnable onLoginSuccess;
    private Runnable onSignUpRequest;

    /**
     * LoginController 생성자
     * View와 DAO를 연결하고 이벤트 리스너를 설정합니다.
     *
     * @param view LoginView 객체
     * @param dao UserDAO 객체
     */
    public LoginController(LoginView view, UserDAO dao) {
        this.view = view;
        this.dao = dao;

        addListeners();
    }

    /**
     * View의 버튼들에 이벤트 리스너를 추가합니다.
     */
    private void addListeners() {
        // 로그인 버튼 클릭 시
        view.getLoginButton().addActionListener(e -> handleLogin());

        // 회원가입 버튼 클릭 시
        view.getSignUpButton().addActionListener(e -> handleSignUpRequest());
    }

    /**
     * 로그인 버튼 클릭 시 호출되는 메소드
     * 입력값을 검증하고 DB에서 인증을 수행합니다.
     */
    private void handleLogin() {
        String userId = view.getUserId();
        String password = view.getPassword();

        // 1. 입력값 검증
        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "아이디와 비밀번호를 입력해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. DB에서 로그인 검증
        boolean authenticated = dao.authenticateUser(userId, password);

        if (authenticated) {
            // 3. 로그인 성공
            Session.setCurrentUserId(userId);
            view.clearFields();

            JOptionPane.showMessageDialog(view,
                userId + "님, 환영합니다!",
                "로그인 성공",
                JOptionPane.INFORMATION_MESSAGE);

            // 4. 메인 화면으로 전환 (콜백 실행)
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            // 5. 로그인 실패
            JOptionPane.showMessageDialog(view,
                "아이디 또는 비밀번호가 올바르지 않습니다.",
                "로그인 실패",
                JOptionPane.ERROR_MESSAGE);
            view.clearFields();
            view.focusUserId();
        }
    }

    /**
     * 회원가입 버튼 클릭 시 호출되는 메소드
     * 회원가입 화면으로 전환합니다.
     */
    private void handleSignUpRequest() {
        if (onSignUpRequest != null) {
            onSignUpRequest.run();
        }
    }

    /**
     * 로그인 성공 시 실행할 콜백을 설정합니다.
     *
     * @param callback 실행할 콜백 (Runnable)
     */
    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    /**
     * 회원가입 버튼 클릭 시 실행할 콜백을 설정합니다.
     *
     * @param callback 실행할 콜백 (Runnable)
     */
    public void setOnSignUpRequest(Runnable callback) {
        this.onSignUpRequest = callback;
    }
}


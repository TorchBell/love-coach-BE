package com.torchbell.lovecoach.common.interceptor;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler)
            throws Exception {
        // OPTIONS 요청은 통과 (CORS)
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebSessionKey.LOGIN_USER_ID.getKey()) == null) {
            throw new BusinessLogicException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.");
        }

        return true;
    }
}

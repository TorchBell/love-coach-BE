package com.torchbell.lovecoach.user.service;

import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.user.dao.UserDao;
import com.torchbell.lovecoach.user.dto.request.CreditUsageRequest;
import com.torchbell.lovecoach.user.dto.request.UserJoinRequest;
import com.torchbell.lovecoach.user.dto.request.UserLoginRequest;
import com.torchbell.lovecoach.user.dto.request.UserUpdateRequest;
import com.torchbell.lovecoach.user.dto.response.CreditUsageResponse;
import com.torchbell.lovecoach.user.dto.response.UserInfoResponse;
import com.torchbell.lovecoach.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserDao userDao;

    // 로그아웃은 UserService 안찍음

    // 유저 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponse info(Long userId) {
        User user = userDao.selectUserById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        return UserInfoResponse.from(user);
    }


    // 회원가입
    @Transactional
    public void join(UserJoinRequest request) {
        // 회원가입시에 반환하는 정보 없음
        if (userDao.selectUserByEmail(request.getEmail()).isPresent()) {
            throw new BusinessLogicException(ErrorCode.DUPLICATE_RESOURCE, "이미 회원가입한 유저입니다.");
        }
        User user = request.toEntity();
        userDao.insertUser(user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public UserInfoResponse login(UserLoginRequest request) {
        User user = userDao.selectUserByEmail(request.getEmail())
                .orElseThrow(()->new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessLogicException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
        if (!user.getIsActive()){
            throw new BusinessLogicException(ErrorCode.BAD_REQUEST, "탈퇴한 회원입니다.");
        }
        return UserInfoResponse.from(user);
    }

    // 크레딧 사용
    @Transactional
    public CreditUsageResponse creditUsage(CreditUsageRequest request, Long userId) {
        User user = userDao.selectUserById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        if (user.getCredit() < request.getAmount()) {
            throw new BusinessLogicException(ErrorCode.BAD_REQUEST, "크레딧이 충분하지 않습니다");
        }
        userDao.decreaseCredit(userId, request.getAmount());

        return new CreditUsageResponse(user.getCredit() - request.getAmount());

    }


    // 내 정보 수정
    @Transactional
    public UserInfoResponse updateUser(UserUpdateRequest request, Long userId) {
        User user = userDao.selectUserById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessLogicException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다");
        }
        User newUserInfo = request.toEntity(userId);

        userDao.updateUser(newUserInfo);
        newUserInfo.setCredit(user.getCredit());

        return UserInfoResponse.from(newUserInfo);
    }

    @Transactional
    public void deleteUser(Long userId){
        User user = userDao.selectUserById(userId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        userDao.deleteUser(userId);
    }



    // 비밀번호는 암호화되어서 들어가기 때문에 따로 만들어야 할 듯...
}

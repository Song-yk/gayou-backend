package com.gayou.auth.service;

import com.gayou.auth.dto.UserDto;
import com.gayou.auth.model.User;
import com.gayou.auth.model.AccountStatus;
import com.gayou.auth.repository.UserRepository;
import com.gayou.auth.exception.UserNotFoundException;
import com.gayou.auth.exception.InvalidCredentialsException;
import com.gayou.auth.exception.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gayou.auth.dto.LoginResponse;
import com.gayou.settings.provider.JwtProvider;

import java.util.Date;
import java.util.List;
import java.security.SecureRandom;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 인코더

    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    // 랜덤 비밀번호 생성을 위한 문자 배열 (숫자, 대문자, 소문자, 특수문자 포함)
    private static final char[] rndAllCharacters = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '@', '$', '!', '%', '*', '?', '&'
    };

    /**
     * 사용자 이름으로 사용자 인증 정보를 로드하는 메서드
     *
     * @param username - 사용자의 사용자 이름 (username)
     * @return UserDetails - Spring Security에서 사용하는 사용자 인증 정보
     * @throws UsernameNotFoundException - 사용자 이름을 찾을 수 없는 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    /**
     * 사용자를 인증하고 JWT 토큰을 발급하는 메서드
     *
     * @param userDto - 사용자의 로그인 정보 (username 또는 email, password)
     * @return LoginResponse - 인증에 성공하면 JWT 토큰을 반환
     * @throws InvalidCredentialsException - 비밀번호가 일치하지 않을 경우 예외 발생
     * @throws UserNotFoundException       - 사용자를 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    public LoginResponse authenticate(UserDto userDto) {
        User user = userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            user.setLastLoginTime(new Date());
            user.setStatus(AccountStatus.ACTIVE);
            userRepository.save(user);

            String token = jwtProvider.create(user.getEmail());
            return new LoginResponse(user.getName(), token);
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    /**
     * 새로운 사용자를 등록하는 메서드 (회원가입)
     *
     * @param userDto - 사용자의 회원가입 정보
     * @throws UserAlreadyExistsException - 사용자 이름이나 이메일이 이미 존재하는 경우 예외 발생
     */
    @Transactional
    public void register(UserDto userDto) {
        if (userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setName(userDto.getName());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setEmail(userDto.getEmail());
        newUser.setPhoneNumber(userDto.getPhoneNumber());
        newUser.setBirthday(userDto.getBirthday());
        newUser.setLastLoginTime(new Date());
        newUser.setStatus(AccountStatus.ACTIVE);

        userRepository.save(newUser);
    }

    /**
     * 랜덤한 비밀번호를 생성하는 메서드
     *
     * @param length - 생성할 비밀번호의 길이
     * @return String - 생성된 랜덤 비밀번호
     */
    public String getRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        int rndAllCharactersLength = rndAllCharacters.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(rndAllCharacters[random.nextInt(rndAllCharactersLength)]);
        }

        return stringBuilder.toString();
    }

    /**
     * 카카오 로그인 처리를 위한 메서드
     *
     * @param email - 카카오에서 받은 사용자의 이메일
     * @return LoginResponse - 로그인 성공 시 JWT 토큰 반환
     * @throws UserNotFoundException - 사용자를 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    public LoginResponse kakaoLogin(String email) {
        if (!userRepository.findByEmail(email).isPresent()) {
            User newUser = new User();
            newUser.setUsername(email.split("@")[0]);
            newUser.setName(email.split("@")[0]);
            newUser.setPassword(passwordEncoder.encode(passwordEncoder.encode(getRandomPassword(10))));
            newUser.setEmail(email);
            newUser.setLastLoginTime(new Date());
            newUser.setStatus(AccountStatus.ACTIVE);

            userRepository.save(newUser);
        }

        User user = userRepository.findByUsernameOrEmail("", email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setLastLoginTime(new Date());
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        String token = jwtProvider.create(user.getEmail());
        return new LoginResponse(user.getName(), token);
    }

    /**
     * 사용자를 삭제하는 메서드 (토큰을 기반으로 사용자 식별)
     *
     * @param token - JWT 토큰 (사용자 식별을 위해 사용)
     */
    @Transactional
    public void deleteUser(String token) {
        // String username = jwtProvider.extractUsername(token);
        // User user = userRepository.findByUsernameOrEmail(username, "")
        // .orElseThrow(() -> new UserNotFoundException("User not found"));

        // userRepository.delete(user);
    }

    /**
     * 30일 동안 비활성화된 사용자를 휴먼 계정으로 전환하는 메서드
     *
     * @Scheduled(cron = "0 0 0 * * ?") - 매일 자정에 실행되도록 설정
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void checkInactiveUsers() {
        Date now = new Date();
        List<User> inactiveUsers = userRepository.findAllByStatusAndLastLoginTimeBefore(AccountStatus.ACTIVE,
                new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000)); // 30일 기준

        for (User user : inactiveUsers) {
            user.setStatus(AccountStatus.SUSPENDED); // 휴먼 계정으로 전환
            userRepository.save(user);
        }
    }

    /**
     * 사용자의 상세 정보를 반환하는 메서드
     *
     * @param token - JWT 토큰 (사용자 식별을 위해 사용)
     * @return UserDto - 사용자 정보
     */
    public UserDto getUserDetails(String token) {
        // String username = jwtProvider.extractUsername(token);
        // User user = userRepository.findByUsernameOrEmail(username, "")
        // .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserDto userDto = new UserDto();
        // userDto.setId(user.getId());
        // userDto.setUsername(user.getUsername());
        // userDto.setName(user.getName());
        // userDto.setEmail(user.getEmail());
        // userDto.setPhoneNumber(user.getPhoneNumber());
        // userDto.setBirthday(user.getBirthday());

        return userDto;
    }

    /**
     * 특정 사용자의 프로필 정보를 반환하는 메서드
     *
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return UserDto - 사용자 프로필 정보
     * @throws UserNotFoundException - 사용자를 찾을 수 없는 경우 예외 발생
     */
    public UserDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setBirthday(user.getBirthday());
        userDto.setGender(user.getGender());
        userDto.setIsLocal(user.isLocal());
        userDto.setProfilePicture(user.getProfilePicture());
        userDto.setDescription(user.getDescription());

        return userDto;
    }

    /**
     * 사용자의 프로필을 업데이트하는 메서드
     *
     * @param userDto - 업데이트할 사용자 정보
     * @throws RuntimeException - 사용자를 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    public void updateProfile(UserDto userDto) {

        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getUsername() != null) {
            existingUser.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userDto.getPhoneNumber());
        }
        if (userDto.getBirthday() != null) {
            existingUser.setBirthday(userDto.getBirthday());
        }
        existingUser.setGender(userDto.getGender());
        existingUser.setLocal(userDto.getIsLocal());
        existingUser.setProfilePicture(userDto.getProfilePicture());
        existingUser.setDescription(userDto.getDescription());

        userRepository.save(existingUser);
    }

    /**
     * 사용자의 비밀번호를 변경하는 메서드
     *
     * @param userDto - 비밀번호를 변경할 사용자 정보
     * @throws RuntimeException - 사용자를 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    public void passwordChange(UserDto userDto) {

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 현재 비밀번호가 맞는지 확인
        // if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
        // throw new IllegalArgumentException("Current password is incorrect");
        // }

        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        userRepository.save(user);
    }
}

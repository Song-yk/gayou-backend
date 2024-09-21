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
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    // UserDetailsService 인터페이스 구현
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    @Transactional
    public LoginResponse authenticate(UserDto userDto) {
        User user = userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            // 로그인 시 마지막 로그인 시간 업데이트 및 계정 상태 복구
            user.setLastLoginTime(new Date());
            user.setStatus(AccountStatus.ACTIVE);
            userRepository.save(user);

            String token = jwtProvider.create(user.getUsername());
            return new LoginResponse(user.getUsername(), token);
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

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
        newUser.setLastLoginTime(new Date()); // 회원가입 시 현재 시간을 마지막 로그인 시간으로 설정
        newUser.setStatus(AccountStatus.ACTIVE); // 초기 상태를 ACTIVE로 설정

        userRepository.save(newUser);
    }

    @Transactional
    public void deleteUser(String token) {
        // String username = jwtProvider.extractUsername(token);
        // User user = userRepository.findByUsernameOrEmail(username, "")
        // .orElseThrow(() -> new UserNotFoundException("User not found"));

        // userRepository.delete(user);
    }

    // 휴먼 계정 전환 기능
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
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

    public UserDto getUserDetails(String token) {
        // String username = jwtProvider.extractUsername(token);
        // User user = userRepository.findByUsernameOrEmail(username, "")
        // .orElseThrow(() -> new UserNotFoundException("User not found"));

        // // User 엔티티를 UserDto로 변환
        UserDto userDto = new UserDto();
        // userDto.setId(user.getId());
        // userDto.setUsername(user.getUsername());
        // userDto.setName(user.getName());
        // userDto.setEmail(user.getEmail());
        // userDto.setPhoneNumber(user.getPhoneNumber());
        // userDto.setBirthday(user.getBirthday());

        return userDto;
    }
    
    public UserDto getUserProfile(Long id) {
        User user = userRepository.findById(id)
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
    
    @Transactional
    public void passwordChange(UserDto userDto) {

        User user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));


     // 현재 비밀번호가 맞는지 확인
//        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("Current password is incorrect");
//        }//여기 안됨...


        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        userRepository.save(user);
    }
}




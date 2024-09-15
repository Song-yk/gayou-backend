package com.gayou.auth.model;

public enum AccountStatus {
    ACTIVE, // 활성화된 계정
    SUSPENDED, // 휴먼 계정 (일정 기간 동안 활동 없음)
    DEACTIVATED // 비활성화된 계정 (탈퇴한 계정 등)
}

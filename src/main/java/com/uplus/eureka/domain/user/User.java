package com.uplus.eureka.domain.user;

import lombok.Data;

@Data
public class User {
    private Integer userId; // 사용자 ID
    private String name;    // 사용자 이름
    private String profile; // 사용자 프로필
}

package com.sparta.milestone.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String chkPw;
    private boolean admin = false;
    private String adminToken = "";
}
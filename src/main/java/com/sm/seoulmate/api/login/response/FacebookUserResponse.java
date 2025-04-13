package com.sm.seoulmate.api.login.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacebookUserResponse {
    private String id;
    private String name;
    private String email;
}

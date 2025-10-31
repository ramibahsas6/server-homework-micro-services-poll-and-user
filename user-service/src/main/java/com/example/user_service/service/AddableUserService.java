package com.example.user_service.service;

import com.example.user_service.model.User;

public interface AddableUserService {
    User isRegisterByUserId(Long user_id);
}

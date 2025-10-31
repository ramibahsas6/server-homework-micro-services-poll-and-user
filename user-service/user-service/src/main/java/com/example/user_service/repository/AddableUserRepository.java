package com.example.user_service.repository;

import com.example.user_service.model.User;

public interface AddableUserRepository {
    User isRegisterByUserId(Long user_id);
}

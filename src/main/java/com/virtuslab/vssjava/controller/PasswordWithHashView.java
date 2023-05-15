package com.virtuslab.vssjava.controller;

import com.virtuslab.vssjava.domain.Password;

record PasswordWithHashView(String hashType, String password, String hash) {

    static PasswordWithHashView fromPassword(Password password) {
        return new PasswordWithHashView(password.hashType(), password.password(), password.hash());
    }
}

package com.virtuslab.vssjava.events;

import com.virtuslab.vssjava.domain.Password;

public record PasswordSavedEvent(String hashType, String password, String hash) {

    public static PasswordSavedEvent fromPassword(Password password) {
        return new PasswordSavedEvent(password.hashType(), password.password(), password.hash());
    }
}

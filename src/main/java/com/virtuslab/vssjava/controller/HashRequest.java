package com.virtuslab.vssjava.controller;

public class HashRequest {
    private final String hashType;
    private final String password;

    public HashRequest(String hashType, String password) {
        this.hashType = hashType;
        this.password = password;
    }

    public String getHashType() {
        return hashType;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "HashResponse{" +
                "hashType='" + hashType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

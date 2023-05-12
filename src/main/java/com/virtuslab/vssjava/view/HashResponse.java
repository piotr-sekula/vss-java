package com.virtuslab.vssjava.view;

public class HashResponse {
    private final String hashType;
    private final String password;
    private final String hash;

    public HashResponse(String hashType, String password, String hash) {
        this.hashType = hashType;
        this.password = password;
        this.hash = hash;
    }

    public String getHashType() {
        return hashType;
    }

    public String getPassword() {
        return password;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "HashResponse{" +
                "hashType='" + hashType + '\'' +
                ", password='" + password + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}

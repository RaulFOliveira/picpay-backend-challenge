package com.example.picpay_challenge.domain.user;

public enum UserRole {
    common("common"),
    shopkeeper("shopkeeper");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}

package com.application.todoapi.common.request;

public record LoginRequest(
    String email,
    String password
) {}

package com.cgnpc.bbxpark.localauth.service;

@FunctionalInterface
public interface LocalLoginGateway {

    void login(String username, String password);
}

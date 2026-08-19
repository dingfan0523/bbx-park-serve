package com.cgnpc.bbxpark.localauth.service;

@FunctionalInterface
public interface AuthenticatedUserProvider {

    String currentUserId();
}

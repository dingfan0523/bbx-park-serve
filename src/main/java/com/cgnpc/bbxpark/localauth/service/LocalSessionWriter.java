package com.cgnpc.bbxpark.localauth.service;

@FunctionalInterface
public interface LocalSessionWriter {

    void store(String userId);
}

package com.cgnpc.bbxpark.localauth.model;

import com.cgnpc.cud.shiro.domain.Account;

public class LocalAccount implements Account {

    private static final long serialVersionUID = 1L;

    private final String account;
    private final String password;

    public LocalAccount(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getPassword() {
        return password;
    }
}

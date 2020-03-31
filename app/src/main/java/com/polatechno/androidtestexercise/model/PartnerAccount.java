package com.polatechno.androidtestexercise.model;

import java.io.Serializable;
import java.util.List;

public class PartnerAccount implements Serializable {
    private String Login;
    private String Password;
    private String passkey;


    public PartnerAccount() {
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }
}

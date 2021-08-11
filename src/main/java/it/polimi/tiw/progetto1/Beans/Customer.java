package it.polimi.tiw.progetto1.Beans;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 24345443454L;

    private String name;
    private String surname;
    private String email;
    private String address;
    private String password;
    private String sex;

    public Customer(String name, String surname, String email, String address, String password, String sex) {
        this.name = name;
        this.surname = surname;
        this. email = email;
        this.address = address;
        this. sex = sex;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }
}

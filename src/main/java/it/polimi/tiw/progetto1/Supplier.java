package it.polimi.tiw.progetto1;

import java.io.Serializable;

public class Supplier implements Serializable {
    private static final long serialVersionUID = 43455657554L;

    private String code;
    private String name;
    private String password;
    private String evaluation;


    public Supplier(String code, String name, String password) {
        this.code = code;
        this.name = name;
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}

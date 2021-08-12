package it.polimi.tiw.progetto1.Beans;

import java.io.Serializable;

public class Supplier implements Serializable {
    private static final long serialVersionUID = 43455657554L;

    private String code;
    private String name;
    private String password;
    private int evaluation;
    private float priceProd;
    private float freeShipPrice;

    public Supplier(String code, String name, String password, int evaluation) {
        this.code = code;
        this.name = name;
        this.password = password;
        this.evaluation = evaluation;
    }

    public float getFreeShipPrice() {
        return freeShipPrice;
    }

    public float getPriceProd() {
        return priceProd;
    }

    public Supplier(String code, String name, int evaluation, float freeShipPrice, float priceProd) {
        this.code = code;
        this.name = name;
        this.evaluation = evaluation;
        this.freeShipPrice = freeShipPrice;
        this.priceProd = priceProd;
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

    public int getEvaluation() {
        return evaluation;
    }

}

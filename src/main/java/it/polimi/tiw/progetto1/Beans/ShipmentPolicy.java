package it.polimi.tiw.progetto1.Beans;

import java.io.Serializable;

public class ShipmentPolicy implements Serializable {
    private static final long serialVersionUID = 546575367554L;

    private int id;
    private int min_articles;
    private int max_articles;
    private String supplier;
    private float costShipment;
    private float freeShipment;

    public ShipmentPolicy(int id, int min_articles, int max_articles, String supplier, float costShipment, float freeShipment) {
        this.id = id;
        this.min_articles = min_articles;
        this.max_articles = max_articles;
        this.supplier = supplier;
        this.costShipment = costShipment;
        this.freeShipment = freeShipment;
    }

    public int getId() {
        return id;
    }

    public int getMin_articles() {
        return min_articles;
    }

    public int getMax_articles() {
        return max_articles;
    }

    public String getSupplier() {
        return supplier;
    }

    public float getCostShipment() {
        return costShipment;
    }

    public float getFreeShipment() {
        return freeShipment;
    }
}
package it.polimi.tiw.progetto1;

import java.io.Serializable;

public class Ship_Policy implements Serializable {
    private static final long serialVersionUID = 4334566947873L;

    private int id;
    private float min_price;
    private float max_price;
    private Supplier supplier;
    private float cost_shipment;
    private float free_shipment;

    public Ship_Policy(int id, float min_price, float max_price, Supplier supplier, float cost_shipment, float free_shipment) {
        this.id = id;
        this.min_price = min_price;
        this.max_price = max_price;
        this.supplier = supplier;
        this.cost_shipment = cost_shipment;
        this.free_shipment = free_shipment;
    }

    public int getId() {
        return id;
    }

    public float getMin_price() {
        return min_price;
    }

    public float getMax_price() {
        return max_price;
    }

    public float getCost_shipment() {
        return cost_shipment;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public float getFree_shipment() {
        return free_shipment;
    }
}

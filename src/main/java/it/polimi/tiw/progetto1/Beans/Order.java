package it.polimi.tiw.progetto1.Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 8726474585097L;

    private List<Product> products;
    private String supplier;

    public Order(String supplier) {
        this.products = new ArrayList<>();
        this.supplier = supplier;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getSupplier() {
        return supplier;
    }
}

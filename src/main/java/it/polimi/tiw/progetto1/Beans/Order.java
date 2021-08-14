package it.polimi.tiw.progetto1.Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 8726474585097L;

    private List<Product> products;
    private String supplierCode;
    private String supplierName;

    public Order(String supplierCode) {
        this.products = new ArrayList<>();
        this.supplierCode = supplierCode;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }
}

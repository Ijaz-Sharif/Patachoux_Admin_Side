package com.patachadmin.patachoux.Model;

public class Product {

    String productName,ProductPrice,ProductDes,ProductImage;

    public Product(String productName, String productPrice, String productDes, String productImage) {
        this.productName = productName;
        ProductPrice = productPrice;
        ProductDes = productDes;
        ProductImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public String getProductDes() {
        return ProductDes;
    }

    public String getProductImage() {
        return ProductImage;
    }
}

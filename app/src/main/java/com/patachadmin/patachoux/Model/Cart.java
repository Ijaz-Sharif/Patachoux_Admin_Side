package com.patachadmin.patachoux.Model;

public class Cart {
    String productName,ProductPrice,ProductQuantity,ProductImage;

    public Cart(String productName, String productPrice, String ProductQuant, String productImage) {
        this.productName = productName;
        ProductPrice = productPrice;
        this.ProductQuantity = ProductQuant;
        ProductImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public String getProductImage() {
        return ProductImage;
    }
}

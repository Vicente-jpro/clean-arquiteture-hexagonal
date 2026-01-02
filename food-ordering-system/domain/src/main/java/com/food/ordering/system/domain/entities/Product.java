package com.food.ordering.system.domain.entities;

import com.food.ordering.system.domain.common.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;

import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId>{

	//private ProductId id;
	private String name;
	private Money price;
	
	public Product(ProductId id, String name, Money price) {
		super.setId(id);
		this.name = name;
		this.price = price;
	}
	
	public Product (ProductId productId) {
		super.setId(productId);
	}
	
	public void updateWithConfirmedNameAndPrice(Product product) {
		this.name = product.getName();
		this.price = product.getPrice();
	}
	
}

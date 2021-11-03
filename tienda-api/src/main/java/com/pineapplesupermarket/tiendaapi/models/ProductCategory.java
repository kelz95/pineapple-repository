package com.pineapplesupermarket.tiendaapi.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ps_product_category")
public class ProductCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_product_category")
	private Integer idProductCategory;
	
	@Column(name = "code", length = 30, unique = true, nullable = false)
	private String code;
	
	@Column(name = "description", length = 50, nullable = false)
	private String description;
	
	
	public long getIdProductCategory() {
		return idProductCategory;
	}
	public void setIdProductCategory(Integer idProductCategory) {
		this.idProductCategory = idProductCategory;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.projections.SellerSummaryProjection;

public class SellerSummaryDTO {

	private String sellerName;
	private Double total;
	
	public SellerSummaryDTO() {
	}

	public SellerSummaryDTO(String sellerName, Double total) {
		this.sellerName = sellerName;
		this.total = total;
	}
	
	public SellerSummaryDTO(SellerSummaryProjection projection) {
		sellerName = projection.getName();
		total = projection.getTotal();
	}

	public String getSellerName() {
		return sellerName;
	}

	public Double getTotal() {
		return total;
	}
	
}

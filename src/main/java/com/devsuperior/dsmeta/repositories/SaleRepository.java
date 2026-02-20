package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SellerSummaryProjection;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	@Query(value = "SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(obj.id, obj.date, obj.amount, obj.seller.name) FROM Sale obj "
			+ "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) "
			+ "AND obj.date BETWEEN :minDate AND :maxDate",
			countQuery = "SELECT count(obj) FROM Sale obj "
					+ "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) "
					+ "AND obj.date BETWEEN :minDate AND :maxDate")
	Page<SaleReportDTO> searchReport(Pageable pageable, String name, LocalDate minDate, LocalDate maxDate);	
	
	@Query(nativeQuery = true, value = "SELECT tb_seller.name, ROUND(SUM(tb_sales.amount), 1) AS total FROM tb_seller "
			+ "INNER JOIN tb_sales ON tb_seller.id = tb_sales.seller_id "
			+ "WHERE tb_sales.date BETWEEN :minDate AND :maxDate "
			+ "GROUP BY tb_seller.id")
	Page<SellerSummaryProjection> searchSummary(Pageable pageable, LocalDate minDate, LocalDate maxDate);
}

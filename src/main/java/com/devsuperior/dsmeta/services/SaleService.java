package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SellerSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SellerSummaryProjection;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	@Transactional(readOnly = true)
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public Page<SaleReportDTO> searchReport(Pageable pageable, String minDate, String maxDate, String name){
		LocalDate min = parseMinDate(minDate);
		LocalDate max = parseMaxDate(maxDate);
		Page<SaleReportDTO> result = repository.searchReport(pageable, name, min, max);
		return result;
	}
	
	@Transactional(readOnly = true)
	public Page<SellerSummaryDTO> searchSummary(Pageable pageable, String minDate, String maxDate){
		LocalDate min = parseMinDate(minDate);
		LocalDate max = parseMaxDate(maxDate);
		
		Page<SellerSummaryProjection> list = repository.searchSummary(pageable, min, max);
		Page<SellerSummaryDTO> result = list.map(x -> new SellerSummaryDTO(x));
		return result;
	}
	
	//métodos auxiliares
	private LocalDate parseMinDate(String date) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate yearAgo = today.minusYears(1L);
		
		LocalDate newMinDate = (date.equals("") || date == null) ? yearAgo : LocalDate.parse(date);
		return newMinDate;
	}
	
	private LocalDate parseMaxDate(String date) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());		
		LocalDate newMaxDate = (date.equals("") || date == null) ? today : LocalDate.parse(date);
		return newMaxDate;
	}
}

package com.santander.crm.sinergia.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.crm.sinergia.entity.Prospecto;
import com.santander.crm.sinergia.filter.ProspectoFilter;
import com.santander.crm.sinergia.response.AltaProspectoRes;
import com.santander.crm.sinergia.response.ConsultaProspectosRes;
import com.santander.crm.sinergia.service.ProspectoService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@RequestMapping("/sinergia")
public class ProspectoController {

	@Autowired
	@Qualifier("prospectoServiceImpl")
	ProspectoService prospectoService;

	@RequestMapping(value = "/prospectos/{idEjecutivo}", method = { RequestMethod.GET })
	@CrossOrigin(origins = "*")
	public ResponseEntity<ConsultaProspectosRes> prospectos(@RequestParam(value = "filter", required = false) String filter, @PathVariable Integer idEjecutivo)
			throws IOException {

		ProspectoFilter pFilter = new ProspectoFilter();
		if (filter != null) {
			pFilter = new ObjectMapper().readValue(filter, ProspectoFilter.class);
		}

		ConsultaProspectosRes response = prospectoService.getProspectosByFilter(pFilter);
		
		HttpStatus hs = response.getHttpStatus();
		HttpHeaders header = new HttpHeaders();
		header.add("errorMessage", response.getMessage());

		return new ResponseEntity<ConsultaProspectosRes>(response, header, hs);
	}
	
	@RequestMapping(value = "/prospectos", method = { RequestMethod.POST })
	@CrossOrigin(origins = "*")
	public ResponseEntity<Prospecto> guardarProspecto(@RequestBody Prospecto prospecto) {
		AltaProspectoRes res = prospectoService.saveProspecto(prospecto);
		
		HttpStatus hs = res.getHttpStatus();
		HttpHeaders header = new HttpHeaders();
		header.add("errorMessage", res.getMessage());
	
		Prospecto response = res.getProspecto();

		return new ResponseEntity<Prospecto>(response, header, hs);
	}

}

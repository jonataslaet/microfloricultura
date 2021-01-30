package br.com.jonataslaet.microservice.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.jonataslaet.microservice.loja.model.Compra;
import br.com.jonataslaet.microservice.loja.service.CompraService;

@RestController
@RequestMapping("/compras")
public class CompraController {
	
	@Autowired
	private CompraService compraService;
	
	@RequestMapping(method=RequestMethod.POST)
	public Compra realizaCompra(@RequestBody CompraDTO compraDTO) {
		return compraService.realizaCompra(compraDTO);
	}
}

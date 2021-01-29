package br.com.jonataslaet.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jonataslaet.microservice.loja.controller.CompraDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoFornecedor;

@Service
public class CompraService {
	
	@Autowired
	private RestTemplate client;
	
	public void realizaCompra(CompraDTO compra) {
		
		ResponseEntity<InfoFornecedor> exchange = client.exchange("http://fornecedor/info/"+compra.getEndereco().getEstado(), HttpMethod.GET, null, InfoFornecedor.class);
		System.out.println(exchange.getBody().getEndereco());
	}

}

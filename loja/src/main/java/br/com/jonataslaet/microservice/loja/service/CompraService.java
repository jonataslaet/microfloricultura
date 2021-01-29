package br.com.jonataslaet.microservice.loja.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.jonataslaet.microservice.loja.controller.CompraDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoFornecedor;

@Service
public class CompraService {
	
	public void realizaCompra(CompraDTO compra) {
		
		RestTemplate client = new RestTemplate();
		ResponseEntity<InfoFornecedor> exchange = client.exchange("http://localhost:8081/info/"+compra.getEndereco().getEstado(), HttpMethod.GET, null, InfoFornecedor.class);
		System.out.println(exchange.getBody().getEndereco());
	}

}

package br.com.jonataslaet.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.jonataslaet.microservice.loja.client.FornecedorClient;
import br.com.jonataslaet.microservice.loja.controller.CompraDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoFornecedorDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoPedidoDTO;
import br.com.jonataslaet.microservice.loja.model.Compra;

@Service
public class CompraService {
	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallBack")
	public Compra realizaCompra(CompraDTO compra) {
		String estado = compra.getEndereco().getEstado();
		LOG.info("Buscando informações do fornecedor do estado {}", estado);
		InfoFornecedorDTO infoFornecedorDTO = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		
		LOG.info("Realizando pedido");
		InfoPedidoDTO pedidoRealizado = fornecedorClient.realizaPedido(compra.getItens());
		
		System.out.println(infoFornecedorDTO.getEndereco());
		
		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(pedidoRealizado.getId());
		compraSalva.setTempoDePreparo(pedidoRealizado.getTempoDePreparo());
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
		
		return compraSalva;
	}
	
	public Compra realizaCompraFallBack(CompraDTO compra) {
		Compra compraFallBack = new Compra();
		compraFallBack.setEnderecoDestino(compra.getEndereco().toString());
		return compraFallBack;
	}

}

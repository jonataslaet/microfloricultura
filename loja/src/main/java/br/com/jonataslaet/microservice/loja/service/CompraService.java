package br.com.jonataslaet.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jonataslaet.microservice.loja.client.FornecedorClient;
import br.com.jonataslaet.microservice.loja.controller.CompraDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoFornecedorDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoPedidoDTO;
import br.com.jonataslaet.microservice.loja.model.Compra;

@Service
public class CompraService {
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	public Compra realizaCompra(CompraDTO compra) {
		InfoFornecedorDTO infoFornecedorDTO = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		InfoPedidoDTO pedidoRealizado = fornecedorClient.realizaPedido(compra.getItens());
		
		System.out.println(infoFornecedorDTO.getEndereco());
		
		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(pedidoRealizado.getId());
		compraSalva.setTempoDePreparo(pedidoRealizado.getTempoDePreparo());
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
		
		return compraSalva;
	}

}

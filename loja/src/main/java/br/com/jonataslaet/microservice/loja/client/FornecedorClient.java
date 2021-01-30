package br.com.jonataslaet.microservice.loja.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.jonataslaet.microservice.loja.controller.dto.InfoFornecedorDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoPedidoDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.ItemCompraDTO;

@FeignClient("fornecedor")
public interface FornecedorClient {

	@RequestMapping("/info/{estado}")
	InfoFornecedorDTO getInfoPorEstado(@PathVariable String estado);

	@RequestMapping(method = RequestMethod.POST, value = "/pedidos")
	InfoPedidoDTO realizaPedido(List<ItemCompraDTO> itens);
	
}

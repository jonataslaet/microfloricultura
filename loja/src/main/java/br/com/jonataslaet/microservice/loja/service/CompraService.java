package br.com.jonataslaet.microservice.loja.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.jonataslaet.microservice.loja.client.FornecedorClient;
import br.com.jonataslaet.microservice.loja.client.TransportadorClient;
import br.com.jonataslaet.microservice.loja.controller.CompraDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoEntregaDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoFornecedorDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.InfoPedidoDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.VoucherDTO;
import br.com.jonataslaet.microservice.loja.model.Compra;
import br.com.jonataslaet.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {
	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	@Autowired
	private TransportadorClient transportadorClient;
	
	@HystrixCommand(threadPoolKey = "recuperaCompraThreadPool")
	public Compra recuperaCompra(Long idCompra) {
		Compra compra = compraRepository.findById(idCompra).orElse(null);
		return compra;
	}
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallBack", threadPoolKey = "realizaCompraThreadPool")
	public Compra realizaCompra(CompraDTO compra) {
		String estado = compra.getEndereco().getEstado();
		LOG.info("Buscando informações do fornecedor do estado {}", estado);
		InfoFornecedorDTO infoFornecedorDTO = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		
		LOG.info("Realizando pedido");
		InfoPedidoDTO pedidoRealizado = fornecedorClient.realizaPedido(compra.getItens());
		
		System.out.println(infoFornecedorDTO.getEndereco());
		
		InfoEntregaDTO entregaDto = new InfoEntregaDTO();
		entregaDto.setPedidoId(pedidoRealizado.getId());
		entregaDto.setDataParaEntrega(LocalDate.now().plusDays(pedidoRealizado.getTempoDePreparo()));
		entregaDto.setEnderecoOrigem(infoFornecedorDTO.getEndereco());
		entregaDto.setEnderecoDestino(compra.getEndereco().toString());
		
		VoucherDTO voucher = transportadorClient.reservaEntrega(entregaDto);
		
		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(pedidoRealizado.getId());
		compraSalva.setTempoDePreparo(pedidoRealizado.getTempoDePreparo());
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
		compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
		compraSalva.setVoucher(voucher.getNumero());
		
		LOG.info("Salvando compra no banco de dados da loja");
		compraRepository.save(compraSalva);
		
		return compraSalva;
	}
	
	public Compra realizaCompraFallBack(CompraDTO compra) {
		Compra compraFallBack = new Compra();
		compraFallBack.setEnderecoDestino(compra.getEndereco().toString());
		return compraFallBack;
	}

}

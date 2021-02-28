package br.com.jonataslaet.microservice.loja.service;

import java.time.LocalDate;
import java.util.Optional;

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
import br.com.jonataslaet.microservice.loja.model.enums.CompraStatus;
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
	
	public Compra reprocessaCompra(Long id) {
		return null;
	}
	
	public Compra cancelaCompra(Long id) {
		return null;
	}
	
	@HystrixCommand(threadPoolKey = "recuperaCompraThreadPool")
	public Compra recuperaCompra(Long idCompra) {
		Compra compra = compraRepository.findById(idCompra).orElse(null);
		return compra;
	}
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallBack", threadPoolKey = "realizaCompraThreadPool")
	public Compra realizaCompra(CompraDTO compra) {
		Compra compraSalva = new Compra();
		LOG.info("Salvando compra no banco de dados da loja");
		compraSalva.setStatus(CompraStatus.RECEBIDO);
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
		compraRepository.save(compraSalva);		
		compra.setCompraId(compraSalva.getId());
		
		String estado = compra.getEndereco().getEstado();
		LOG.info("Buscando informações do fornecedor do estado {}", estado);
		InfoFornecedorDTO infoFornecedorDTO = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		System.out.println(infoFornecedorDTO.getEndereco());
		
		LOG.info("Realizando pedido ao fornecedor");
		InfoPedidoDTO pedidoRealizado = fornecedorClient.realizaPedido(compra.getItens());
		compraSalva.setPedidoId(pedidoRealizado.getId());
		compraSalva.setTempoDePreparo(pedidoRealizado.getTempoDePreparo());
		compraSalva.setStatus(CompraStatus.PEDIDO_REALIZADO);
		compraRepository.save(compraSalva);
		
		if (1==1) {
			throw new RuntimeException();
		}
		
		InfoEntregaDTO entregaDto = new InfoEntregaDTO();
		entregaDto.setPedidoId(pedidoRealizado.getId());
		entregaDto.setDataParaEntrega(LocalDate.now().plusDays(pedidoRealizado.getTempoDePreparo()));
		entregaDto.setEnderecoOrigem(infoFornecedorDTO.getEndereco());
		entregaDto.setEnderecoDestino(compra.getEndereco().toString());
		
		LOG.info("Reservando entrega com o transportador");
		VoucherDTO voucher = transportadorClient.reservaEntrega(entregaDto);
		compraSalva.setStatus(CompraStatus.ENTREGA_RESERVADA);
		compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
		compraSalva.setVoucher(voucher.getNumero());
		compraRepository.save(compraSalva);
		
		return compraSalva;
	}
	
	public Compra realizaCompraFallBack(CompraDTO compra) {
		LOG.info("Chamando fall back para realizaCompra");
		if (compra.getCompraId() != null) {
			Optional<Compra> compraOptional = compraRepository.findById(compra.getCompraId());
			if (compraOptional.isPresent()) {
				return compraOptional.get();
			}
			else {
				Compra compraFallBack = new Compra();
				compraFallBack.setEnderecoDestino(compra.getEndereco().toString());
			}
		}
		
		return null;
	}

}

package br.com.jonataslaet.microservice.loja.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.jonataslaet.microservice.loja.controller.dto.EnderecoDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.ItemCompraDTO;

public class CompraDTO {

	@JsonIgnore
	private Long compraId;
	private List<ItemCompraDTO> itens;
	private EnderecoDTO endereco;
	
	public Long getCompraId() {
		return compraId;
	}
	public void setCompraId(Long compraId) {
		this.compraId = compraId;
	}
	public List<ItemCompraDTO> getItens() {
		return itens;
	}
	public void setItens(List<ItemCompraDTO> itens) {
		this.itens = itens;
	}
	public EnderecoDTO getEndereco() {
		return endereco;
	}
	public void setEndereco(EnderecoDTO endereco) {
		this.endereco = endereco;
	}
	
	
}

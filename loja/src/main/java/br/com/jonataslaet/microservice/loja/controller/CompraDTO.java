package br.com.jonataslaet.microservice.loja.controller;

import java.util.List;

import br.com.jonataslaet.microservice.loja.controller.dto.EnderecoDTO;
import br.com.jonataslaet.microservice.loja.controller.dto.ItemCompraDTO;

public class CompraDTO {

	private List<ItemCompraDTO> itens;
	private EnderecoDTO endereco;
	
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

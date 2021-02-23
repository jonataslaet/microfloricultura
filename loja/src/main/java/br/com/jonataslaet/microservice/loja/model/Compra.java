package br.com.jonataslaet.microservice.loja.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Compra {

	@Id
	private Long pedidoId;
	private Integer tempoDePreparo;
	private String enderecoDestino;
	private LocalDate dataParaEntrega;
	private Long voucher;
	
	public Long getPedidoId() {
		return pedidoId;
	}
	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}
	public Integer getTempoDePreparo() {
		return tempoDePreparo;
	}
	public void setTempoDePreparo(Integer tempoDePreparo) {
		this.tempoDePreparo = tempoDePreparo;
	}
	public String getEnderecoDestino() {
		return enderecoDestino;
	}
	public void setEnderecoDestino(String enderecoDestino) {
		this.enderecoDestino = enderecoDestino;
	}
	public LocalDate getDataParaEntrega() {
		return this.dataParaEntrega;
	}
	public void setDataParaEntrega(LocalDate previsaoParaEntrega) {
		this.dataParaEntrega = previsaoParaEntrega;
	}
	public Long getVoucher() {
		return voucher;
	}
	public void setVoucher(Long numero) {
		this.voucher = numero;
	}
	
	
}

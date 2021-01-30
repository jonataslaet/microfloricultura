package br.com.jonataslaet.microservice.fornecedor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jonataslaet.microservice.fornecedor.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}

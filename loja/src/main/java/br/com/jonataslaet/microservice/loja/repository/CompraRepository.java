package br.com.jonataslaet.microservice.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jonataslaet.microservice.loja.model.Compra;

public interface CompraRepository extends JpaRepository <Compra, Long>{

}

package br.edu.ifpr.paranavai.poswebsys.core.dominio;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CidadeRepositorio extends JpaRepository<Cidade, Long> {

	@Query("from Cidade where nome = :nome")
	Collection<Cidade> findAllByNome(@Param("nome") String nome);
}

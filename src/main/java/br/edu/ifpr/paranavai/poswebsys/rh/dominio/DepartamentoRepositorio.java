package br.edu.ifpr.paranavai.poswebsys.rh.dominio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartamentoRepositorio extends JpaRepository<Departamento, Long> {
	
	@Query("select d from Departamento d where lower(d.nome) like lower(concat(:termo, '%'))")
	List<Departamento> searchByNome(@Param("termo") String termo);
}

package br.edu.ifpr.paranavai.poswebsys.rh.dominio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos.PessoaListaDTO;

@Repository
public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {
	
	@Query("select new br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos.PessoaListaDTO(p.id, p.nome, p.telefone, p.email, d.nome)"
			+ "from Pessoa p left join p.departamento d")
	List<PessoaListaDTO> findAllPessoaLista();
	
	@Query(value = "select new br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos.PessoaListaDTO(p.id, p.nome, p.telefone, p.email, d.nome)"
			+ "from Pessoa p left join p.departamento d ", countQuery = "select count(p) from Pessoa p")
	Page<PessoaListaDTO> findAllPessoaListaPaginado(Pageable pageable);
	
	@Query("select p from Pessoa p left join fetch p.cidade c left join fetch p.departamento d where p.id = :indice")
	Optional<Pessoa> findCompletoById(@Param("indice") Long id);
}

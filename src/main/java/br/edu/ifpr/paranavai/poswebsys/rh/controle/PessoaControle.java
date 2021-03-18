package br.edu.ifpr.paranavai.poswebsys.rh.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.edu.ifpr.paranavai.poswebsys.core.dominio.Cidade;
import br.edu.ifpr.paranavai.poswebsys.core.dominio.CidadeRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.Departamento;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.DepartamentoRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.Pessoa;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.PessoaRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos.AutoCompleteDTO;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos.PessoaListaDTO;

@Controller
public class PessoaControle {
	
	private PessoaRepositorio pessoaRepo;
	private CidadeRepositorio cidadeRepo;
	private DepartamentoRepositorio departamentoRepo;
	
	private List<Departamento> departamentosFiltrados = new ArrayList<>();
	private List<Cidade> cidadesFiltradas = new ArrayList<>();
	
	public PessoaControle(PessoaRepositorio pessoaRepo, CidadeRepositorio cidadeRepo, DepartamentoRepositorio departamentoRepo) {
		this.pessoaRepo = pessoaRepo;
		this.cidadeRepo = cidadeRepo;
		this.departamentoRepo = departamentoRepo;
	}
	
	@GetMapping("/rh/pessoas")
	public String pessoas(Model model, @RequestParam("page") Optional<Integer> pagina, @RequestParam("size") Optional<Integer> tamanho) {
		int paginaAtual = pagina.orElse(1) - 1;
		int tamanhoPagina = tamanho.orElse(5);
		
		PageRequest requisicao = PageRequest.of(paginaAtual, tamanhoPagina, Sort.by("nome"));
		Page<PessoaListaDTO> listaPaginada = pessoaRepo.findAllPessoaListaPaginado(requisicao);
		
		model.addAttribute("listaPessoas", listaPaginada);

		int totalPaginas = listaPaginada.getTotalPages();
		if (totalPaginas > 0) {
			List<Integer> numerosPaginas = IntStream.rangeClosed(1, totalPaginas)
						.boxed()
						.collect(Collectors.toList());
			model.addAttribute("numerosPaginas", numerosPaginas);
		}
		
		return "rh/pessoas/index";
	}
	
	@GetMapping("/rh/pessoas/nova")
	public String novaPessoa(Model model) {
		
		model.addAttribute("pessoa", new Pessoa(""));
		
		return "rh/pessoas/form";
	}
	
	@GetMapping("/rh/pessoas/{id}")
	public String alterarPessoa(@PathVariable("id") long id, Model model) {
		Optional<Pessoa> pessoaOpt = pessoaRepo.findCompletoById(id);
		if (!pessoaOpt.isPresent()) {
			throw new IllegalArgumentException("Pessoa inválida.");
		}
		
		model.addAttribute("pessoa", pessoaOpt.get());
		
		return "rh/pessoas/form";
	}
	
	@PostMapping("/rh/pessoas/salvar")
	public String salvarPessoa(@Valid @ModelAttribute("pessoa") Pessoa pessoa, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "rh/pessoas/form";
		}
		
		pessoaRepo.save(pessoa);
		return "redirect:/rh/pessoas";
	}
	
	@GetMapping("/rh/pessoas/excluir/{id}")
	public String excluirPessoa(@PathVariable("id") long id) {
		Optional<Pessoa> pessoaOpt = pessoaRepo.findById(id);
		if (pessoaOpt.isEmpty()) {
			throw new IllegalArgumentException("Pessoa inválida.");
		}
		
		pessoaRepo.delete(pessoaOpt.get());
		return "redirect:/rh/pessoas";
	}
	
	@RequestMapping("/rh/pessoas/cidadesNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> cidadesNomeAutoComplete(@RequestParam(value="term", required = false, defaultValue = "") String term) {
		List<AutoCompleteDTO> sugestoes = new ArrayList<>();
		
		try {
			if(term.length() >= 3) {
				cidadesFiltradas = cidadeRepo.searchByNome(term);
			}
			
			for (Cidade cidade : cidadesFiltradas) {
				if (cidade.getNome().toLowerCase().contains(term.toLowerCase())) {
					AutoCompleteDTO dto = new AutoCompleteDTO(cidade.getNomeUF(), Long.toString(cidade.getId()));
					sugestoes.add(dto);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sugestoes;
	}
	
	@RequestMapping("/rh/pessoas/departamentosNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> departamentosNomeAutoComplete(@RequestParam(value="term", required = false, defaultValue="") String term) {
		List<AutoCompleteDTO> sugestoes = new ArrayList<>();
		try {
			if (term.length() >= 3) {
				departamentosFiltrados = departamentoRepo.searchByNome(term);
			}
			
			for (Departamento departamento : departamentosFiltrados) {
				if (departamento.getNome().toLowerCase().contains(term.toLowerCase())) {
					sugestoes.add(new AutoCompleteDTO(departamento.getNome(), Long.toString(departamento.getId())));	
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return sugestoes;
	}
}

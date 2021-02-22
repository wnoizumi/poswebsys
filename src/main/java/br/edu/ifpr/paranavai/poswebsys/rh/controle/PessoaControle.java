package br.edu.ifpr.paranavai.poswebsys.rh.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import br.edu.ifpr.paranavai.poswebsys.core.dominio.CidadeRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.Departamento;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.DepartamentoRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.Pessoa;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.PessoaRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos.DepartamentoSelecaoDTO;

@Controller
public class PessoaControle {
	
	private PessoaRepositorio pessoaRepo;
	private CidadeRepositorio cidadeRepo;
	private DepartamentoRepositorio departamentoRepo;
	
	private List<Departamento> departamentosFiltrados = new ArrayList<>();
	private String primeirosTresCaracteres;
	
	public PessoaControle(PessoaRepositorio pessoaRepo, CidadeRepositorio cidadeRepo, DepartamentoRepositorio departamentoRepo) {
		this.pessoaRepo = pessoaRepo;
		this.cidadeRepo = cidadeRepo;
		this.departamentoRepo = departamentoRepo;
	}
	
	@GetMapping("/rh/pessoas")
	public String pessoas(Model model) {
		model.addAttribute("listaPessoas", pessoaRepo.findAll());
		return "rh/pessoas/index";
	}
	
	@GetMapping("/rh/pessoas/nova")
	public String novaPessoa(Model model) {
		
		model.addAttribute("pessoa", new Pessoa(""));
		model.addAttribute("cidades", cidadeRepo.findAll());
		
		return "rh/pessoas/form";
	}
	
	@GetMapping("/rh/pessoas/{id}")
	public String alterarPessoa(@PathVariable("id") long id, Model model) {
		Optional<Pessoa> pessoaOpt = pessoaRepo.findById(id);
		if (pessoaOpt.isEmpty()) {
			throw new IllegalArgumentException("Pessoa inválida.");
		}
		
		model.addAttribute("pessoa", pessoaOpt.get());
		model.addAttribute("cidades", cidadeRepo.findAll());
		
		return "rh/pessoas/form";
	}
	
	@PostMapping("/rh/pessoas/salvar")
	public String salvarPessoa(@Valid @ModelAttribute("pessoa") Pessoa pessoa, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("cidades", cidadeRepo.findAll());
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
	
	@RequestMapping("/rh/pessoas/departamentosNomeAutoComplete")
	@ResponseBody
	public List<DepartamentoSelecaoDTO> departamentosNomeAutoComplete(@RequestParam(value="term", required = false, defaultValue="") String term) {
		List<DepartamentoSelecaoDTO> sugestoes = new ArrayList<>();
		try {
			if (term.length() >= 3) {
				primeirosTresCaracteres = term;
				departamentosFiltrados = departamentoRepo.findByNome(term);
			}
			
			for (Departamento departamento : departamentosFiltrados) {
				if (departamento.getNome().toLowerCase().contains(term.toLowerCase())) {
					sugestoes.add(new DepartamentoSelecaoDTO(departamento.getNome(), departamento.getId()));
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return sugestoes;
	}
}

package br.edu.ifpr.paranavai.poswebsys.rh.controle;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.ifpr.paranavai.poswebsys.core.dominio.Cidade;
import br.edu.ifpr.paranavai.poswebsys.core.dominio.CidadeRepositorio;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.Pessoa;
import br.edu.ifpr.paranavai.poswebsys.rh.dominio.PessoaRepositorio;

@Controller
public class PessoaControle {
	
	private PessoaRepositorio pessoaRepo;
	private CidadeRepositorio cidadeRepo;
	
	public PessoaControle(PessoaRepositorio pessoaRepo, CidadeRepositorio cidadeRepo) {
		this.pessoaRepo = pessoaRepo;
		this.cidadeRepo = cidadeRepo;
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
}

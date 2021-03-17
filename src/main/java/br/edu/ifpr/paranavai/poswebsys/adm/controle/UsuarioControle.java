package br.edu.ifpr.paranavai.poswebsys.adm.controle;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.ifpr.paranavai.poswebsys.adm.dominio.Role;
import br.edu.ifpr.paranavai.poswebsys.adm.dominio.Usuario;
import br.edu.ifpr.paranavai.poswebsys.adm.dominio.UsuarioRepositorio;
import br.edu.ifpr.paranavai.poswebsys.adm.dominio.dtos.RoleUsuarioForm;

@Controller
public class UsuarioControle {
	
	private PasswordEncoder encoder;
	private UsuarioRepositorio usuarioRepo;
	private final List<String> roles;
	
	public UsuarioControle(UsuarioRepositorio usuarioRepo, PasswordEncoder encoder) {
		this.encoder = encoder;
		this.usuarioRepo = usuarioRepo;
		this.roles = Role.getRoles();
	}
	
	@GetMapping("/login")
	public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "/login";
    }
	
	@GetMapping("/adm/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("listaUsuarios", usuarioRepo.findAll());
		return "adm/usuarios/index";
	}
	
	@GetMapping("/adm/usuarios/novo")
	public String novoUsuario(Model model) {
		
		model.addAttribute("usuario", new Usuario("", ""));
		model.addAttribute("roles", roles);
		
		return "adm/usuarios/novo";
	}
	
	@PostMapping("/adm/usuarios/salvar")
	public String salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, Model model) {
		
		Usuario usuarioEncontrado = usuarioRepo.findByUsername(usuario.getUsername());
		if (usuarioEncontrado != null && usuarioEncontrado.getId() != usuario.getId()) {
			bindingResult.addError(new FieldError("usuario", "username", "Nome de usuário já está em uso."));
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roles);
			return "adm/usuarios/novo";
		}
		
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		
		usuarioRepo.save(usuario);
		return "redirect:/adm/usuarios";
	}
	
	@GetMapping("/adm/usuarios/excluir/{id}")
	public String excluirUsuario(@PathVariable("id") long id) {
		Optional<Usuario> usuarioOpt = usuarioRepo.findById(id);
		if (usuarioOpt.isEmpty()) {
			throw new IllegalArgumentException("Usuário inválido.");
		}
		
		usuarioRepo.delete(usuarioOpt.get());
		return "redirect:/adm/usuarios";
	}
	
	@GetMapping("/adm/usuarios/alterar/role/{id}")
	public String getAlterarPapelUsuario(@PathVariable("id") long id, Model model) {
		Optional<Usuario> usuarioOpt = usuarioRepo.findById(id);
		if (!usuarioOpt.isPresent()) {
			throw new IllegalArgumentException("Usuário inválido.");
		}
		
		RoleUsuarioForm roleUsuarioForm = new RoleUsuarioForm(usuarioOpt.get());
		
		model.addAttribute("roleUsuarioForm", roleUsuarioForm);
		model.addAttribute("roles", roles);
		
		return "adm/usuarios/alterar_role";
	}
	
	@PostMapping("/adm/usuarios/alterar/role")
	public String alterarPapelUsuario(@Valid @ModelAttribute("roleUsuarioForm") RoleUsuarioForm roleUsuarioForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roles);
			return "adm/usuarios/alterar_role";
		}
		
		Usuario usuarioAlterado = usuarioRepo.findById(roleUsuarioForm.getId()).orElseThrow(() -> new InvalidParameterException("Usuário Inválido!"));
		usuarioAlterado.setRole(roleUsuarioForm.getRole());
		
		usuarioRepo.save(usuarioAlterado);
		
		return "redirect:/adm/usuarios";
	}
}
package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repositoy.UsuarioRepository;

/*Para liberar apenas um End-Point retira essa anotacao*/
//@CrossOrigin(origins = "*")/*apenas a anotacao CrossOrigin por padrao libera tudo*/
/*pode se passar um array de url ex: origin={"teste.com.br", teste2.com.br}*/
@RestController /*Arquitetura Rest*/
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	//Passando duas variaveis na url
	@GetMapping(value = "/{id}/codigoVenda{venda}", produces = "application/pdf")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id, 
			@PathVariable(value="venda") Long venda) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "v1/{id}", produces = "application/json")/*versionamento pela uri*/
	public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		System.out.println("Executando versão 1");
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}", produces = "application/json", headers="X-API-version=v2")/*versionamento pelo header*/
	public ResponseEntity<Usuario> initV2(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		System.out.println("Executando versão 2");
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String deletar(@PathVariable(value = "id") Long id) {
		
		usuarioRepository.deleteById(id);
		return "Deletado com Sucesso!";
				
	}
	/*para deixar acesso a todos basta deixar apenas a anotacao @CrossOrigin*/
	//@CrossOrigin(origins = "so vai acessar quem vier desta url www.teste.com.br")
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario(){
	
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	//@CrossOrigin(origins = "localhost:8080")
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){

		for(int pos=0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());
		if(!userTemporario.getLogin().equals(usuario.getLogin())) { /*Senhas diferentes*/
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
		}
		
		Usuario usuarioAlterado = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioAlterado, HttpStatus.OK);
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		
		for(int pos=0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/{idUser}/idVenda{idVenda}", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarVenda(@PathVariable Long idUser, @PathVariable Long idVenda){
		
		//Usuario usuarioCadastrado = usuarioRepository.save(usuario);
		return new ResponseEntity("Id usuario: " + idUser + "Id Venda: " + idVenda, HttpStatus.OK);
	}
}

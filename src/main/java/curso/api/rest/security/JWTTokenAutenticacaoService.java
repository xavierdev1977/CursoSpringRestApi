package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContexLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repositoy.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/*Tempo de validade do token*/
	private static final long EXPIRATION_TIME = 172800000;
	
	/*Uma senha única para compor a autentiicação*/
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
	/*Prefixo padrão de token*/
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*Gerando token de autenticado e adicionando ao cabeçalho e resposta http*/
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		/*Montagem do Token*/
		String JWT = Jwts.builder()/*Chama o gerador de token*/
				.setSubject(username)/*Adiciona o usuário*/
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))/*Tempo de expiração*/
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();/*Compactação e algoritimo de geração de senha*/
		
		/*Junta token com prefixo*/
		String token = TOKEN_PREFIX + " " + JWT;/*bearer 5354456565768787878*/
		
		/*Adiciona no cabeçalho http*/
		response.addHeader(HEADER_STRING, token);/*Authorization: bearer 5354456565768787878*/
		
		/*Escreve token como resposta no corpo http*/
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");		
	}
	
	/*Retorna o usuário validade com token ou caso não seja válido retorna null*/
	public Authentication getAuthentication(HttpServletRequest request) {
		
		/*Pega o token enviado no cabeçalho http*/
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			
			/*Faz a validação do token do usuário na requisição*/
			String user = Jwts.parser().setSigningKey(SECRET)/*Vem assim: bearer 5354456565768787878*/
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))/*Depois fica assim: 5354456565768787878*/
					.getBody().getSubject();/*Rodrigo Xavier*/
			
			if(user != null) {
				
				Usuario usuario = ApplicationContexLoad.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				/*Verifica se usuário existe para retornar o usuario logado*/
				if(usuario != null) {
				
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getPassword(),
							usuario.getAuthorities());
			
				}
			}
		}
		return null;/*Não autorizado*/
	}
}

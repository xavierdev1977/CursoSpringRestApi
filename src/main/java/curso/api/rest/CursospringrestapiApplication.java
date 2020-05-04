package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"curso.api.rest.model"})
@ComponentScan(basePackages = {"curso.*"})
@EnableJpaRepositories(basePackages = {"curso.api.rest.repositoy"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
	
	//Mapeamento global que refletem em todo sistema
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/usuario/**").allowedMethods("*").allowedOrigins("*");//Liberando o mapeamento de usuario para todas as origens
		//registry.addMapping("*/**");//Com esta anotacao esta dando acesso a todos os controler e end-point
		//registry.addMapping("*/usuario/**");//Com esta anotacao esta dando acesso a tudo do /usuario
		//registry.addMapping("*/usuario/**").allowedMethods("*");//Com esta anotacao esta liberando todos os metodos do /usuario
		//registry.addMapping("*/usuario/**").allowedMethods("POST","PUT","DELETE");//Com esta anotacao esta dando acesso a um determinado metodo
		//registry.addMapping("*/usuario/**").allowedMethods("POST").allowedOrigins("www.teste.com.br","www.teste2.com.br");//Com esta anotacao esta dando acesso apenas ao metodo post e vindo de uma determinada url ou varias
		//Na aula configuracao centralizado do mudolo 36 tem um exemplo de liberar varios metodos e varias origens
	}	

}


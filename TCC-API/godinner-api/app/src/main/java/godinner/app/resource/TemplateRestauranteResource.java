package godinner.app.resource;

import java.util.List;

import javax.annotation.processing.SupportedOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import godinner.app.config.JwtTokenUtill;
import godinner.app.helper.Template;
import godinner.app.model.Produto;
import godinner.app.model.Restaurante;
import godinner.app.model.TemplateRestaurante;
import godinner.app.repository.ProdutoRepository;
import godinner.app.repository.RestauranteRepository;
import godinner.app.repository.TemplateRestauranteRepository;

@RestController
@RequestMapping("/templates")
@CrossOrigin(origins = "http://localhost:3000")
public class TemplateRestauranteResource {
	
	@Autowired
	private TemplateRestauranteRepository templateRestauranteRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository; 
		
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public List<TemplateRestaurante> getTemplates() {
		return templateRestauranteRepository.findAll();
	}
	
	@PostMapping
	public TemplateRestaurante setTemplateRestaurante(@Validated @RequestBody TemplateRestaurante tr) {

		Restaurante restaurante = restauranteRepository.getPorId(tr.getRestaurante().getId());
		tr.setRestaurante(restaurante);
		Template template = new Template();
		TemplateRestaurante templateRestaurante = templateRestauranteRepository.save(tr);
		
		Template templateHelper = new Template();
		
		String dominio = tr.getRestaurante().getRazaoSocial().toLowerCase().replaceAll("/[^a-z]/g", "");
		System.out.println(dominio);
		templateHelper.criarHost(dominio, tr.getRestaurante().getId());
		
		return templateRestaurante;
	}
	
	@GetMapping("/restaurantes/{idRestaurante}")
	public TemplateRestaurante getTemplate(@PathVariable int idRestaurante) {
		TemplateRestaurante templateRestaurante = templateRestauranteRepository.getTemplate(idRestaurante);
		if(templateRestaurante != null) {
			templateRestaurante.getRestaurante().setSenha(null);			
		}
		return templateRestaurante;
		
	}
	
	@PutMapping
	public TemplateRestaurante putTemplate(@Validated @RequestBody TemplateRestaurante template) {
		templateRestauranteRepository.save(template);
		return template = templateRestauranteRepository.getTemplateById(template.getId());
	}
	
	@GetMapping("/maisvendidos/{id}")
	public List<Produto> getProdutosMaisVendidos(@PathVariable int id){
		return produtoRepository.getProdutosMaisVendidos(id);
	}



}

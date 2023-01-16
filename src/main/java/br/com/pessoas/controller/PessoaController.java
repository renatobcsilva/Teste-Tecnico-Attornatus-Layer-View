package br.com.pessoas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.pessoas.model.Pessoa;
import br.com.pessoas.repository.PessoaRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@GetMapping("/pessoas")
	 public String getAll(Model model, @Param("keyword") String keyword) {
		try {
		      List<Pessoa> pessoas = new ArrayList<Pessoa>();

		      if (keyword == null) {
		        pessoaRepository.findAll().forEach(pessoas::add);
		      } else {
		        pessoaRepository.findByNomeContainingIgnoreCase(keyword).forEach(pessoas::add);
		        model.addAttribute("keyword", keyword);
		      }

		      model.addAttribute("pessoas", pessoas);
		    } catch (Exception e) {
		      model.addAttribute("message", e.getMessage());
		    }
	    return "pessoas";
	  }
	
	@GetMapping("/pessoas/new")
	  public String addPessoa(Model model) {
	    Pessoa pessoa = new Pessoa();
	    
	    model.addAttribute("pessoa", pessoa);
	    model.addAttribute("pageTitle", "Cadastrar Nova Pessoa");

	    return "pessoa_form";
	  }
	
	@PostMapping("/pessoas/save")
	  public String savePessoa(Pessoa pessoa , RedirectAttributes redirectAttributes) {
	    
		try {
		      pessoaRepository.save(pessoa);

		      redirectAttributes.addFlashAttribute("message", "Pessoa salva com sucesso!");
		    } catch (Exception e) {
		      redirectAttributes.addAttribute("message", e.getMessage());
		    }
		
	    return "redirect:/pessoas";
	  }
	
	@GetMapping("/pessoas/{id}")
	  public String editPessoa(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		 try {
		      Pessoa pessoa = pessoaRepository.findById(id).get();

		      model.addAttribute("pessoa", pessoa);
		      model.addAttribute("pageTitle", "Editar os dados da pessoa (ID: " + id + ")");

		      return "pessoa_form";
		    } catch (Exception e) {
		      redirectAttributes.addFlashAttribute("message", e.getMessage());

	    return "redirect:/pessoa_form";
	  }
	}
	
	@GetMapping("/pessoas/delete/{id}")
	  public String deletePessoa(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
		      pessoaRepository.deleteById(id);

		      redirectAttributes.addFlashAttribute("message", "A pessoa com id=" + id + " foi deletada!");
		    } catch (Exception e) {
		      redirectAttributes.addFlashAttribute("message", e.getMessage());
		    }
	    return "redirect:/pessoas";
	  }
	
	
}


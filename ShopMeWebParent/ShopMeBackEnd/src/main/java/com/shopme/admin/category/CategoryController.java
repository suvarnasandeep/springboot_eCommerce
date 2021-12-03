package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class CategoryController {

	@Autowired 
	CategoryService categoryService;
	
	@GetMapping("/categories") 
	public String listAll(Model model) { 
		List<Category>listCategory = categoryService.listAll(); 
		
		//System.out.println("----->>>>" + listCategory);
		
		model.addAttribute("listCategory", listCategory);
		return "category/category"; 
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		
		Category category = new Category(); 
		//category.setEnabled(true);
		
		List<Category>listCategories = categoryService.listCategoriesUsedInForm(); 
		
		System.out.println(listCategories);
		
		model.addAttribute("category",category);
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		
		return "category/category_form";
	}
	
	@PostMapping("/categories/save") 
	public String saveCategory(Category category, RedirectAttributes redirectAttributes) {
		
		category.setImage("default.png");
		
		categoryService.save(category);
		
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
		return "redirect:/categories";
		
	}
}

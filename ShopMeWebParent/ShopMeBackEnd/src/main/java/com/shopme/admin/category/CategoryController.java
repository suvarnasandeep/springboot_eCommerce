package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
		
		model.addAttribute("category",category);
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		
		return "category/category_form";
	}
}

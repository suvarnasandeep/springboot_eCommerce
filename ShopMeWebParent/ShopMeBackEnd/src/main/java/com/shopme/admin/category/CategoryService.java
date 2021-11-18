package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository repo;

	public List<Category> listAll() {
		
		return (List<Category>) repo.findAll();
	}
	
	public List<Category>listCategoriesUsedInForm(){
		List<Category> resultCategory = new ArrayList<Category>();
		
		Iterable<Category> categoriesInBD =  repo.findAll();
		
		for(Category category : categoriesInBD) {
			if(category.getParent() == null) {				
				resultCategory.add(new Category(category.getName()));
				
				Set<Category> children = category.getChildern();
				for(Category subCat : children) {					
					resultCategory.add(new Category("--" + subCat.getName()));
					listChildren(subCat, 1, resultCategory);					
				}
			}
		}	
		
		return resultCategory;
	}
	
	private void listChildren(Category parent, int subLevel, List<Category> resultCategory) {
		int newSubLevel = subLevel+1;
		
		Set<Category> children = parent.getChildern();
		
		for(Category subCat : children) {
			String name = "";
			for(int i=0; i<newSubLevel; i++) {				
				name += "--";
			}
			
			resultCategory.add(new Category(name + subCat.getName()));
			
			listChildren(subCat, newSubLevel, resultCategory);
		}
	}
}

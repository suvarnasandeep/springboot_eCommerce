package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(1);
		Category subCategory = new Category("Computer Components", parent);
		Category savedCategory = repo.save(subCategory);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory1() {
		Category parent = new Category(2);
		Category subCategory = new Category("Cameras", parent);
		Category subCategory1 = new Category("Smartphones", parent);
		
		repo.saveAll(List.of(subCategory, subCategory1));		
	}
	
	@Test
	public void testCreateSubCategory2() {
		Category parent = new Category(6);
		Category subCategory = new Category("Nikon", parent);
		Category savedCategory = repo.save(subCategory);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {
		Category categogy = repo.findById(1).get();
		System.out.println(categogy.getName());
		
		Set<Category> children = categogy.getChildern();
		
		for(Category subCat : children) {
			System.out.println(subCat.getName());
		}		
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories =  repo.findAll();
		
		for(Category category : categories) {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildern();
				for(Category subCat : children) {
					System.out.println("--" + subCat.getName());
					printChildren(subCat, 1);					
				}
			}
		}		
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel+1;
		
		Set<Category> children = parent.getChildern();
		for(Category subCat : children) {
			for(int i=0; i<newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCat.getName());
			
			printChildren(subCat, newSubLevel);
		}
	}
}

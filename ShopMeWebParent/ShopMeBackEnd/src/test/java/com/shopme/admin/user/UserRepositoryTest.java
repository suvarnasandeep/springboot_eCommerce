package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager; 
	
	@Test
	public void testCreateUserWithOneRow() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("anup@gmail.com", "zzzxxx", "Anup", "Kumar");
		user.addRole(roleAdmin);
		
		User savedUser = repo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testCreateUserWithTwoRows() {
		
		User user = new User("niriksha@gmail.com", "12345", "niriksha", "suvarna");
		
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		user.addRole(roleEditor);
		user.addRole(roleAssistant);
				
		User savedUser = repo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testRetrieveAllUser() {
		Iterable<User> users = repo.findAll();
		
		users.forEach(user -> System.out.println(user));		
	}
	
	@Test
	public void testGetUSerById() {
		User user = repo.findById(2).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = repo.findById(1).get();
		user.setEnabled(true);
		user.setEmail("sandeep@mail.com");
		
		repo.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User user = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role rolesales = new Role(2);
		user.getRoles().remove(roleEditor);
		user.addRole(rolesales);
		
		repo.save(user);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "anup@gmail.com";
		
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	@Test
	public void testCountById() {
		Integer id = 5;
		
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		
		int pageNumber = 1;
		int pageSize = 5;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page =  repo.findAll(pageable);
		
		List<User> content = page.getContent();
		
		content.forEach(user -> System.out.println(user));
		
		assertThat(content.size()).isEqualTo(pageSize);
	}
}

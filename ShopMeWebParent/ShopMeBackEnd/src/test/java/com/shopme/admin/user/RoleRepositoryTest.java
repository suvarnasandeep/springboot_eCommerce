package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

	@Autowired
	RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role role = new Role("Admin", "Manage everything");
		
		Role savedRole = repo.save(role);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role salesRole = new Role("Salesperson", "Manage product price");
		Role editorRole = new Role("Editor", "Manage catagory brand");
		Role shipperRole = new Role("Shipper", "Manage shipping");
		Role assistantRole = new Role("Assistant", "Manage product review");
		
		repo.saveAll(List.of(salesRole, editorRole, shipperRole, assistantRole));
	}
		
}

package com.shopme.admin.user;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserCOntroller {

	@Autowired
	private UserService userService;

/*
	@GetMapping("/users") 
	public String listAll(Model model) { 
		List<User>
		listUser = userService.listAll(); model.addAttribute("listUser", listUser);
		return "users"; 
	}
*/

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc");
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortOrder") String sortOrder) {
					
		Page<User> pageUser = userService.listByPage(pageNum, sortField, sortOrder);

		List<User> listUser = pageUser.getContent();

		//System.out.println("page Number = " + pageNum);
		//System.out.println("Total element = " + pageUser.getTotalElements());
		//System.out.println("Total page = " + pageUser.getTotalPages());

		long startCount = (pageNum - 1) * userService.USER_PER_PAGE + 1;
		long endCount = startCount + UserService.USER_PER_PAGE - 1;
		if(endCount > pageUser.getTotalElements()) {
			endCount = pageUser.getTotalElements();
		}
		
		String reverseSortOrder = sortOrder.equals("asc") ? "desc" : "asc";
				
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("lastPage", pageUser.getTotalPages());
		model.addAttribute("totalItems", pageUser.getTotalElements());
		model.addAttribute("listUser", listUser);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortOrder", sortOrder);
		model.addAttribute("reverseSortOrder", reverseSortOrder);
		return "users";
	}



	@GetMapping("/users/new")
	public String newUser(Model model) {

		List<Role> listRole = userService.listRole();

		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRole", listRole);
		model.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}

	@PostMapping("/user/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);

		userService.save(user);


		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = userService.get(id);	
			List<Role> listRole = userService.listRole();

			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (Id - " + id + ")");
			model.addAttribute("listRole", listRole);
			return "user_form";

		} catch (UserNotFoundException e) {			
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}		
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);	
			redirectAttributes.addFlashAttribute("message", "The user Id " + id +" has been deleted successfully");		

		} catch (UserNotFoundException e) {			
			redirectAttributes.addFlashAttribute("message", e.getMessage());			
		}
		return "redirect:/users";		
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable(name = "id") Integer id, 
			@PathVariable(name = "status") boolean enabled, 
			RedirectAttributes redirectAttributes) {
		userService.updateUserEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : " disabled";

		redirectAttributes.addFlashAttribute("message", "The user id " + id + " has been " + status);

		return "redirect:/users";

	}
}

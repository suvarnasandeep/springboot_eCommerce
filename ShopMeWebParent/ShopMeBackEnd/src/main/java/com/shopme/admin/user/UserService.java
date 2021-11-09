package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {

	public static final int USER_PER_PAGE = 5;
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		
		//return (List<User>) userRepo.findAll();
		
		//sort by firstname assending order
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}
	
	public Page<User> listByPage(int pageNum, String sortField, String sortOrder, String keyWord){
		
		Sort sort = Sort.by(sortField);
		
		sort = sortOrder.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sort);
		
		if(keyWord != null) {
			return userRepo.findAll(keyWord, pageable);
		} 
		
		return userRepo.findAll(pageable);			
		
	}
	
	public List<Role> listRole(){
		return (List<Role>) roleRepo.findAll();
	}

	public void save(User user) {
		
		boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		
		userRepo.save(user);
		
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id , String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		System.out.println("sandeep-->" + userByEmail);
		
		if(userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if(userByEmail != null) return false;
		} else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}		
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}		
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
		userRepo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		
		userRepo.updateEnabledStatus(id, enabled);;
	}
	
	
}

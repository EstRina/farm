package farming.accounting.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farming.accounting.dto.RolesResponseDto;
import farming.accounting.dto.UserRequestDto;
import farming.accounting.dto.UserResponseDto;
import farming.accounting.service.IAccountingManagement;

@RestController
@RequestMapping("/account")
public class AccountingController{

	@Autowired
	IAccountingManagement service;

	@PostMapping({"/register", "/register/"})
	public UserResponseDto registration(@RequestBody UserRequestDto user) {
		return service.registration(user);
	}

	@DeleteMapping("/user/{login}")
	public UserResponseDto removeUser(@PathVariable String login) {
		return service.removeUser(login);
	}

	@PostMapping("/login")
	public UserResponseDto getUser(Principal principal) { 
		return service.getUser(principal.getName());
	}

	@PutMapping("/user/{login}")
	public UserResponseDto editUser(@RequestBody UserResponseDto user, @PathVariable String login) {
		return service.editUser(user, login);
	}

	@PutMapping("/password")
	public boolean updatePassword(Principal principal, @RequestHeader("X-New-Password") String password) {
		return service.updatePassword(principal.getName(), password);
	}

	@PutMapping("/revoke/{login}")
	public boolean revokeAccount(@PathVariable String login) {
		return service.revokeAccount(login);
	}

	@PutMapping("/activate/{login}")
	public boolean activateAccount(@PathVariable String login) {
		return service.activateAccount(login);
	}

	@PutMapping("/user/{login}/role/{role}")
	public RolesResponseDto addRole(@PathVariable String login, @PathVariable String role) {
		return service.addRole(login, role);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public RolesResponseDto removeRole(@PathVariable String login, @PathVariable String role) {
		return service.removeRole(login, role);
	}

	@GetMapping("/password/{login}")
	public String getPasswordHash(@PathVariable String login) {
		return service.getPasswordHash(login);
	}

	@GetMapping("/activation_date/{login}")
	public LocalDateTime getActivationDate(@PathVariable String login) {
	return service.getActivationDate(login);
	}

	@GetMapping("/roles/{login}")
	public RolesResponseDto getRoles(@PathVariable String login) {
		return service.getRoles(login);
	}
	
}

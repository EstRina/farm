package farming.accounting.service;

import java.time.LocalDateTime;

import farming.accounting.dto.RolesResponseDto;
import farming.accounting.dto.UserRequestDto;
import farming.accounting.dto.UserResponseDto;

public interface IAccountingManagement {

//	UserResponseDto registration(UserRequestDto user);
	UserResponseDto removeUser(String login);
	UserResponseDto getUser(String login);
	UserResponseDto editUser(UserResponseDto user, String login);
	
	boolean updatePassword(String login, String password);
	boolean revokeAccount(String login);
	boolean activateAccount(String login);
	
	RolesResponseDto addRole(String login, String role);
	RolesResponseDto removeRole(String login, String role);
	
	String getPasswordHash(String login);
	LocalDateTime getActivationDate(String login);
	RolesResponseDto getRoles(String login);
	
	
	UserResponseDto registration(UserRequestDto userDto, boolean isFarmer);
}

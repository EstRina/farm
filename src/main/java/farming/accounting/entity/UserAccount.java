package farming.accounting.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;

import farming.accounting.dto.UserResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
//@Document(collection = "user-accounts")
public class UserAccount {
	
	@Id
	@Setter(value = AccessLevel.NONE)
	private String login;
	
	private String hash;
	private String firstName;
	private String lastName;
	private HashSet<String> roles = new HashSet<String>();
	private LocalDateTime activationDate;
	private boolean revoked;
	private LinkedList<String> lastHash = new LinkedList<String>();
	
	public UserAccount(String login, String hash, String firstName, String lastName) {
		super();
		this.login = login;
		this.hash = hash;
		this.firstName = firstName;
		this.lastName = lastName;
		roles.add("USER");
		activationDate = LocalDateTime.now();
		
	}
	
	public UserAccount() {
		roles.add("USER");
		activationDate = LocalDateTime.now();
	}

	public UserResponseDto build() {
		UserResponseDto dto = new UserResponseDto();
		dto.setLogin(login);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setRoles(roles);
		return dto;
	}

}

// admin -> readWrite -> reader -> user
package farming.accounting.entity;

import java.time.LocalDateTime;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;

import farming.accounting.dto.UserResponseDto;
import farming.accounting.dto.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Table(name = "user-accounts")
public class UserAccount {
	
	@Id
	@Setter(value = AccessLevel.NONE)
	private String login;
	
	private String hash;
	private String firstName;
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	private LocalDateTime activationDate;
	private boolean revoked;
	private LinkedList<String> lastHash = new LinkedList<String>();
	
	public UserAccount(String login, String hash, String firstName, String lastName, UserType userType) {
		super();
		this.login = login;
		this.hash = hash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		activationDate = LocalDateTime.now();
		}
	
	public UserResponseDto build() {
		UserResponseDto dto = new UserResponseDto();
		dto.setLogin(login);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		return dto;
	}

}

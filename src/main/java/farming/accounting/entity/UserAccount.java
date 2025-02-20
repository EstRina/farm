
package farming.accounting.entity;

import java.time.LocalDateTime;
import java.util.LinkedList;

import farming.accounting.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

import farming.accounting.dto.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_account")
public class UserAccount {

   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String login;
	private String hash;
	private String firstName;
	private String lastName;
	private String roles;
	private LocalDateTime activationDate;
	private boolean revoked;

	@Transient
	private LinkedList<String> lastHash = new LinkedList<String>();

	
	public UserAccount(String login, String hash, String firstName, String lastName) {
		super();
		this.login = login;
		this.hash = hash;
		this.firstName = firstName;
		this.lastName = lastName;
		roles = "USER";
		activationDate = LocalDateTime.now();
		
	}
	
	public UserAccount() {
		roles ="USER";
		activationDate = LocalDateTime.now();
	}

}

// admin -> readWrite -> reader -> user

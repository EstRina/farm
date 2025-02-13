package farming.customer.dto;

import farming.farmer.dto.AddressDto;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CustomerDto {

	@Id
	Long customerId;
	String login;
	String password;
	String firstName;
	String lastName;
	String email;
	double balance;
}

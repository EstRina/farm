package farming.customer.entity;

import farming.customer.dto.CustomerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "customers")
public class Customer {
	
	@Id
	Long customerId;
	String login;
	String firstName;
	String lastName;
	String email;
	double balance;
	
	public static Customer of(CustomerDto dto) {
		return Customer.builder().customerId(dto.getCustomerId()).
				firstName(dto.getFirstName()).
				lastName(dto.getLastName()).email(dto.getEmail()).
				build();	
	}
	
	public CustomerDto build() {
		return CustomerDto.builder().customerId(customerId).firstName(firstName).
				lastName(lastName).email(email).build();
	}

}

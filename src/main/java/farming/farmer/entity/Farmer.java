package farming.farmer.entity;

import java.util.Set;

import farming.farmer.dto.AddressDto;
import farming.farmer.dto.FarmerDto;
import farming.products.entity.Product;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "farmers")
public class Farmer {

	@Id
	Long farmerId;
	String firstName;
	String lastName;
	String email;
	String phone;
	
	@Embedded
	AddressDto address;
	
	double balance;
	
	@ManyToMany(mappedBy = "farmers")
	@JoinTable(name = "farmer_products", joinColumns = @JoinColumn(name = "farmer_id"),
	inverseJoinColumns = @JoinColumn(name = "product_id"))
	Set<Product> products;
	
		public static Farmer of(FarmerDto dto) {
		return Farmer.builder().email(dto.getEmail()).firstName(dto.getFirstName()).
				lastName(dto.getLastName()).phone(dto.getPhone()).address(dto.getAddress())
				.build();
	}
	
	public FarmerDto build() {
		return FarmerDto.builder().email(email).lastName(lastName).firstName(firstName).
				phone(phone).address(address)
				.build();
	}
}

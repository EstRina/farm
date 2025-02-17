package farming.products.entity;

import java.util.HashSet;
import java.util.Set;

import farming.customer.entity.Customer;
import farming.products.dto.CartDto;
import farming.products.dto.CartItemDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "carts")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@OneToOne
	@JoinColumn(name = "customer_id", nullable = false, unique = true)
	Long customerId;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<CartItemDto> items;
	
	public static Cart of(CartDto dto, Customer customer) {
		return Cart.builder().id(dto.getCartId()).customerId(dto.getCustomerId()).items(dto.getItems()).build();
	}
	
	public CartDto build() {
		return CartDto.builder().cartId(id).customerId(customerId).items(items).build();
	}
}

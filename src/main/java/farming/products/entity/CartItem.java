package farming.products.entity;

import farming.products.dto.CartItemDto;
import farming.products.dto.ProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cart_items")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false)
	Cart cart;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	ProductDto product;
	
	int quantity;
	
	public static CartItem of(CartItemDto dto, Cart cart) {
		return CartItem.builder().id(dto.getId()).cart(cart).product(dto.getProduct()).quantity(dto.getQuantity()).build();
	}
	
	public CartItemDto build() {
		return CartItemDto.builder().id(id).product(product).quantity(quantity).price(this.product.getPrice()).build();
	}
}

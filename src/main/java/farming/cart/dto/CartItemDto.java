package farming.cart.dto;

import farming.products.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemDto {
	
	Long id;
	ProductDto product;
	int quantity;
	double price;

}

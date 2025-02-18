package farming.cart.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartDto {
	
	Long cartId;
	Long customerId;
	Set<CartItemDto> items;

}

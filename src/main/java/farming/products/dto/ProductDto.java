package farming.products.dto;

import farming.farmer.dto.FarmerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductDto {

	Long productId;
	String productName;
	int quantity;
	Double price;
	String imgUrl;
	FarmerDto farmer;
	
}

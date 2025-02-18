package farming.products.dto;

import java.time.LocalDateTime;

import farming.products.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SaleRecordsDto {

	Product product;
	Long customerId;
	Long farmerId;
	LocalDateTime saleDate;
	int saleQuantity;
	double cost;
	
	

}

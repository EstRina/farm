package farming.products.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RemoveProductDataDto {

	ProductDto product;
	List<SaleRecordsDto> saleRecords;
}

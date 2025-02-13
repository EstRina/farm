package farming.products.entity;

import java.util.List;

import farming.products.dto.ProductDto;
import farming.products.dto.SaleRecordsDto;
import jakarta.persistence.Entity;
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
@Table(name = "remove_product_data")
public class RemoveProductData {

	@OneToOne
	ProductDto product;
	List<SaleRecordsDto> saleRecords;
	
}

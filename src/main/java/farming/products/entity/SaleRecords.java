package farming.products.entity;

import java.time.LocalDateTime;

import farming.products.dto.SaleRecordsDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="Sale_records")
public class SaleRecords {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	Product product;
	
	Long customerId;
	Long farmerId;
	
	@Temporal(TemporalType.DATE)
	LocalDateTime saleDate;
	
	int saleQuantity;
	double cost;
	
	public static SaleRecords of(SaleRecordsDto dto) {
		return SaleRecords.builder().farmerId(dto.getFarmerId()).
				customerId(dto.getCustomerId()).cost(dto.getCost()).
				saleDate(dto.getSaleDate()).saleQuantity(dto.getSaleQuantity()).build();
			}
	
	public SaleRecordsDto build() {
		return SaleRecordsDto.builder().farmerId(farmerId).
				customerId(customerId).cost(cost).
				saleDate(saleDate).saleQuantity(saleQuantity).build();
	}
}

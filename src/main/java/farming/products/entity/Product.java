package farming.products.entity;

import java.util.List;
import org.springframework.data.annotation.Id;

import farming.farmer.dto.FarmerDto;
import farming.farmer.entity.Farmer;
import farming.products.dto.ProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "products")
public class Product {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long productId;
	public String productName;
	public int quantity;
	public Double price;
	public String imgUrl;
	FarmerDto farmer;

	@ManyToMany
	 @JoinTable(
		        name = "farmers_products", joinColumns = @JoinColumn(name = "product_id"), 
		        inverseJoinColumns = @JoinColumn(name = "farmer_id"))
	List<Farmer> farmers;
	
	public static Product of(ProductDto dto) {
		return Product.builder().productId(dto.getProductId()).productName(dto.getProductName()).
				farmer(dto.getFarmer()).
				quantity(dto.getQuantity()).price(dto.getPrice()).imgUrl(dto.getImgUrl()).build();
	}
	
	public ProductDto build() {
		return ProductDto.builder().productId(productId).productName(productName).farmer(farmer).
				quantity(quantity).price(price).imgUrl(imgUrl).build();
	}

}

package farming.products.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import farming.farmer.entity.Farmer;
import farming.products.dto.ProductDto;
import farming.products.dto.RemoveProductDataDto;
import farming.products.dto.SaleRecordsDto;
import farming.products.entity.Product;
import farming.products.entity.SaleRecords;
import farming.repo.FarmerRepositiry;
import farming.repo.ProductsRepository;

@Service
public class ProductsService implements IProductsService{

	@Autowired
	ProductsRepository productRepo;
	@Autowired
	FarmerRepositiry farmerRepo;
	
	@Override
	public boolean addProduct(ProductDto productDto) {
		Product product = new Product();
        product.productName = productDto.getProductName();
        product.quantity = productDto.getQuantity();
        product.price = productDto.getPrice();
        product.imgUrl = productDto.getImgUrl();

        productRepo.save(product);
        return true;
	}

	@Override
	public boolean updateProduct(ProductDto productDto) {
		Product product = productRepo.findById(productDto.getProductId()).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
            
//            product.productName = productDto.getProductName();
            product.quantity = productDto.getQuantity();
            product.price = productDto.getPrice();
//            product.imgUrl = productDto.getImgUrl();

            productRepo.save(product);
            return true;
	}

//	@Override
//	public RemoveProductDataDto removeProduct(Long productId) {
//		Product product = productRepo.findById(productDto.getProductId()).orElseThrow(() ->
//		new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
//            productRepo.delete(product);  // Remove from repository
//            
//            // Create and return the removal information
//            List<SaleRecordsDto> list = product.;
//			return new RemoveProductDataDto(productId, list );
//        }
//        return null; //
//	}

	@Override
	public ProductDto getProduct(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
		return product.build();
	}

	@Override
	public Set<ProductDto> getProductsByFarmer(Long farmerId) {
		Farmer farmer = farmerRepo.findById(farmerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not exsists"));
		return farmer.getProducts().stream().map(Product::build).collect(Collectors.toSet());
	}

	@Override
	public Set<ProductDto> getProductsByPriceRange(double minPrice, double maxPrice, Long productId) {
		    return productRepo.findByPriceBetween(minPrice, maxPrice).stream().map(Product::build).collect(Collectors.toSet());
	}

	@Override
	public List<ProductDto> getAllProducts() {
		return productRepo.findAll().stream().map(Product::build).collect(Collectors.toList());
	}

	@Override
	public SaleRecordsDto buyProduct(Long customerId, Long productId, int quantity) {
		SaleRecords saleRecord = new SaleRecords();
		Product product = productRepo.findById(productId).
				filter(p -> p.getQuantity() >= quantity).orElseThrow(() ->
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
		saleRecord.setCustomerId(customerId);
		saleRecord.setFarmerId(product.getFarmer().getFarmerId()); 
		saleRecord.setSaleDate(LocalDateTime.now());
		saleRecord.setSaleQuantity(quantity);
		saleRecord.setCost(product.getPrice() * quantity);
	          return saleRecord.build();
	           
	}

	@Override
	public List<ProductDto> getSoldProducts(Long farmerId) {
		Farmer farmer = farmerRepo.findById(farmerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not exsists"));
		
		return null;
	}

	@Override
	public List<SaleRecordsDto> getPurchasedProducts(Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RemoveProductDataDto> getHistoryOfRemovedProducts(Long farmerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoveProductDataDto removeProduct(Long productId) {
		// TODO Auto-generated method stub
		return null;
	}

}

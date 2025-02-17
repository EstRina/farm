package farming.products.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import farming.customer.entity.Customer;
import farming.farmer.entity.Farmer;
import farming.products.dto.CartDto;
import farming.products.dto.CartItemDto;
import farming.products.dto.ProductDto;
import farming.products.dto.RemoveProductDataDto;
import farming.products.dto.SaleRecordsDto;
import farming.products.entity.Cart;
import farming.products.entity.Product;
import farming.products.entity.SaleRecords;
import farming.repo.CartItemRepository;
import farming.repo.CartRepository;
import farming.repo.CustomerRepository;
import farming.repo.FarmerRepositiry;
import farming.repo.ProductsRepository;
import farming.repo.SaleRecordsRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductsService implements IProductsService{

	@Autowired
	ProductsRepository productRepo;
	@Autowired
	FarmerRepositiry farmerRepo;
	@Autowired
	SaleRecordsRepository saleRecordsRepo;
	@Autowired
	CustomerRepository customerRepo;
	@Autowired
	CartRepository cartRepo;
	@Autowired
	CartItemRepository cartItemRepo;
	
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
            
            product.productName = productDto.getProductName();
            product.quantity = productDto.getQuantity();
            product.price = productDto.getPrice();
            product.imgUrl = productDto.getImgUrl();

            productRepo.save(product);
            return true;
	}

	@Override
	public RemoveProductDataDto removeProduct(Long productId, Long farmerId) {
		Product product = productRepo.findById(productId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
            if(product.getFarmers().stream().noneMatch(f -> f.getFarmerId().equals(farmerId))) {
            	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer doesn't own this product");
            }
            List<SaleRecords> list = saleRecordsRepo.findByProduct(product);
            List<SaleRecordsDto> sales = list.stream().map(SaleRecords::build).collect(Collectors.toList());
            product.getFarmers().removeIf(f -> f.getFarmerId().equals(farmerId));
            if(product.getFarmers().isEmpty()) {
            	productRepo.delete(product);
            } else {
            	productRepo.save(product);
            }
			return new RemoveProductDataDto(product.build(), sales);
		
      
	}

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
		    return productRepo.findByPriceBetweenAndId(minPrice, maxPrice, productId).stream().map(Product::build).collect(Collectors.toSet());
	}

	@Override
	public List<ProductDto> getAllProducts() {
		return productRepo.findAll().stream().map(Product::build).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public SaleRecordsDto buyProduct(Long customerId, Long productId, int quantity) {
		
		Product product = productRepo.findById(productId).orElseThrow(() ->
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
		if(product.getQuantity() < quantity)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock avaliable");
		product.setQuantity(product.getQuantity() - quantity);
		SaleRecords saleRecord = new SaleRecords();
		saleRecord.setCustomerId(customerId);
		saleRecord.setFarmerId(product.getFarmer().getFarmerId()); 
		saleRecord.setSaleDate(LocalDateTime.now());
		saleRecord.setSaleQuantity(quantity);
		saleRecord.setCost(product.getPrice() * quantity);
		saleRecordsRepo.save(saleRecord);
		productRepo.save(product);
	          return saleRecord.build();
	           
	}

	@Override
	public List<ProductDto> getSoldProducts(Long farmerId) {
		Farmer farmer = farmerRepo.findById(farmerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not exsists"));
		
		return productRepo.findSoldProductByFarmerId(farmerId).stream().map(Product::build).collect(Collectors.toList());
	}

	@Override
	public List<SaleRecordsDto> getPurchasedProducts(Long customerId) {
		Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not exsists"));
		
		return saleRecordsRepo.findById(customerId).stream().map(SaleRecords::build).collect(Collectors.toList());
	}

	@Override
	public List<RemoveProductDataDto> getHistoryOfRemovedProducts(Long farmerId) {
		Farmer farmer = farmerRepo.findById(farmerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not exsists"));
		return productRepo.findRemovedProductsByFarmerId(farmerId).stream().
				map(p -> new RemoveProductDataDto(p.build(), List.of())).collect(Collectors.toList());
	}

	@Override
	public CartDto getCart(Long customerId) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		return cart.build();
	}

//	private Cart createCart(Long customerId) {
//		Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
//		new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not exsists"));
//		Cart cart = 
//		return null;
//	}

	@Override
	@Transactional
	public CartDto addToCart(Long customerId, Long productId, int quantity) {
		Cart cart = cartRepo.findById(customerId).orElseGet(() -> createCart(customerId));
		Product product = productRepo.findById(productId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not exsists"));
		CartItemDto dto = new CartItemDto(null, product.build(), quantity, product.getPrice());
		cart.getItems().add(dto);
		cart = cartRepo.save(cart);
		return cart.build();
	}

	private Cart createCart(Long customerId) {
	// TODO Auto-generated method stub
	return null;
}

	@Override
	@Transactional
	public CartDto removeFromCart(Long customerId, Long productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public CartDto clearCart(Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDto updateCartItemQuantity(Long customerId, Long productId, int newQuantity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkout(Long customerId) {
		// TODO Auto-generated method stub
		return false;
	}


	

}

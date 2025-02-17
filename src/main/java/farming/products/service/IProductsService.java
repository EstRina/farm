package farming.products.service;

import java.util.List;
import java.util.Set;

import farming.products.dto.CartDto;
import farming.products.dto.ProductDto;
import farming.products.dto.RemoveProductDataDto;
import farming.products.dto.SaleRecordsDto;

public interface IProductsService {

	boolean addProduct(ProductDto productDto);
	boolean updateProduct(ProductDto productDto);
	
	ProductDto getProduct(Long productId);
	Set<ProductDto> getProductsByFarmer(Long farmerId);
	Set<ProductDto> getProductsByPriceRange(double minPrice, double maxPrice, Long productId);
	List<ProductDto> getAllProducts();
	
	SaleRecordsDto buyProduct(Long customerId, Long productId, int quantity);
	List<ProductDto> getSoldProducts(Long farmerId);
	List<SaleRecordsDto> getPurchasedProducts(Long customerId);
	List<RemoveProductDataDto> getHistoryOfRemovedProducts(Long farmerId);
	RemoveProductDataDto removeProduct(Long productId, Long farmerId);
	
	CartDto getCart(Long customerId);
	CartDto addToCart(Long customerId, Long productId, int quantity);
	CartDto updateCartItemQuantity(Long customerId, Long productId, int newQuantity);
	CartDto removeFromCart(Long customerId, Long productId);
	CartDto clearCart(Long customerId);
//	double getCartTotal(Long customerId);
	boolean checkout(Long customerId); //это если в корзине висит 10 позиций, а кто то купил часть, то это проверка хватает ли этих позиций
}

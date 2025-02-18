package farming.cart.service;

import farming.cart.dto.CartDto;

public interface ICartService {
	
	CartDto getCart(Long customerId);
	CartDto addToCart(Long customerId, Long productId, int quantity);
	CartDto updateCartItemQuantity(Long customerId, Long productId, int newQuantity);
	CartDto removeFromCart(Long customerId, Long productId);
	CartDto clearCart(Long customerId);
	double getTotalCost(Long customerId); 
	boolean checkout(Long customerId); // это проверка баланса

}

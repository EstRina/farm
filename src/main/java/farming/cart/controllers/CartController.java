package farming.cart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import farming.cart.dto.CartDto;
import farming.cart.service.ICartService;

@RestController
@RequestMapping("/cart")
public class CartController{
	
	@Autowired
	ICartService cartService;

	@GetMapping("/{customerId}")
	public CartDto getCart(@PathVariable Long customerId) {
		return cartService.getCart(customerId);
	}

	@PostMapping("/add")
	public CartDto addToCart(@RequestParam Long customerId, @RequestParam Long productId, 
			@RequestParam int quantity) {
		return cartService.addToCart(customerId, productId, quantity);
	}

	@PutMapping("/update")
	public CartDto updateCartItemQuantity(@RequestParam Long customerId, @RequestParam Long productId, 
			@RequestParam int newQuantity) {
		return cartService.updateCartItemQuantity(customerId, productId, newQuantity);
	}

	@DeleteMapping("/remove")
	public CartDto removeFromCart(@RequestParam Long customerId, @RequestParam Long productId) {
		return cartService.removeFromCart(customerId, productId);
	}

	@DeleteMapping("/clear/{customerId}")
	public CartDto clearCart(@PathVariable Long customerId) {
		return cartService.clearCart(customerId);
	}

	@GetMapping("/totalCost/{customerId}")
	public double getTotalCost(@PathVariable Long customerId) {
		return cartService.getTotalCost(customerId);
	}

	@PostMapping("/checkout/{customerId}")
	public boolean checkout(@RequestParam Long customerId) {
		return cartService.checkout(customerId);
	}

}

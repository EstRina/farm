package farming.cart.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import farming.cart.dto.CartDto;
import farming.cart.dto.CartItemDto;
import farming.cart.entity.Cart;
import farming.customer.entity.Customer;
import farming.products.entity.Product;
import farming.repo.CartItemRepository;
import farming.repo.CartRepository;
import farming.repo.CustomerRepository;
import farming.repo.ProductsRepository;
import jakarta.transaction.Transactional;

@Service
public class CartService implements ICartService{
	
	@Autowired
	CartRepository cartRepo;
	@Autowired
	CartItemRepository cartItemRepo;
	@Autowired
	ProductsRepository productRepo;
	@Autowired
	CustomerRepository customerRepo;
	
	@Override
	public CartDto getCart(Long customerId) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		return cart.build();
	}

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
		Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not exsists"));
		Cart cart = Cart.builder().customerId(customerId).items(new HashSet<>()).build();
	return cartRepo.save(cart);
}

	@Override
	@Transactional
	public CartDto removeFromCart(Long customerId, Long productId) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		cart.getItems().removeIf(i -> i.getId().equals(productId));
		cart = cartRepo.save(cart);
		return cart.build();
	}

	@Override
	@Transactional
	public CartDto clearCart(Long customerId) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		cart.getItems().clear();
		return cart.build();
	}

	@Override
	public CartDto updateCartItemQuantity(Long customerId, Long productId, int newQuantity) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		cart.getItems().stream().filter(i -> i.getId().equals(productId)).findFirst().ifPresent(i -> i.setQuantity(newQuantity));
		cart = cartRepo.save(cart);
		return cart.build();
	}

	@Override
	public boolean checkout(Long customerId) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		if(cart.getItems().isEmpty())
		return false;
		
		double totalCost = getTotalCost(customerId);
		Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not exsists"));
		if(customer.getBalance() < totalCost)
			return false;
		
		customer.setBalance(customer.getBalance() - totalCost);
		customerRepo.save(customer);
		
		cart.getItems().clear();
		cartRepo.save(cart);
		return true;
		
	}

	@Override
	public double getTotalCost(Long customerId) {
		Cart cart = cartRepo.findById(customerId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Curt not found by this id" + customerId));
		return cart.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
	}

}

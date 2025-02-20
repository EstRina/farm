package farming.cart.service;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}

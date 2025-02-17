package farming.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.products.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}

package farming.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.products.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}

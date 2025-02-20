package farming.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}

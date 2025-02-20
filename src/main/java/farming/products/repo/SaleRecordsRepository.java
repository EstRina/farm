package farming.products.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.products.entity.Product;
import farming.products.entity.SaleRecords;

public interface SaleRecordsRepository extends JpaRepository<SaleRecords, Long>{

	List<SaleRecords> findByProduct(Product product);

}

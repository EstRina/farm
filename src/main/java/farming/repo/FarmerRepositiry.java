package farming.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.farmer.entity.Farmer;

public interface FarmerRepositiry extends JpaRepository<Farmer, Long>{

}

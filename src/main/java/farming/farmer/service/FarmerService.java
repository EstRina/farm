package farming.farmer.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import farming.accounting.entity.UserAccount;
import farming.farmer.dto.FarmerDto;
import farming.farmer.entity.Farmer;
import farming.farmer.repo.FarmerRepositiry;
import farming.products.entity.Product;
import farming.products.repo.ProductsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FarmerService implements IFarmerService{
	
	final FarmerRepositiry farmerRepo;
	final ProductsRepository productRepo;
//	final PasswordEncoder passwordEncoder;
	
	@Transactional
	public void createFarmerProfile(UserAccount user) {
		  Farmer farmer = new Farmer();
	        farmer.setLogin(user.getLogin());
	        farmer.setFirstName(user.getFirstName());
	        farmer.setLastName(user.getLastName());
	        farmer.setEmail(user.getLogin()); // Можно добавить email, если он хранится в UserAccount
	        farmerRepo.save(farmer);
	    }
	
	
	@Override
	public FarmerDto getFarmer(Long farmerId) {
		return farmerRepo.findById(farmerId).map(Farmer::build).orElseThrow(() -> 
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));
	}

	@Override
	public Set<FarmerDto> getFarmersByProduct(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow(() ->
		new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
		return product.getFarmers().stream().map(Farmer::build).collect(Collectors.toSet());
	}

	@Override
	public List<FarmerDto> getAllFarmers() {
		return farmerRepo.findAll().stream().map(Farmer::build).collect(Collectors.toList());
	}

	

}

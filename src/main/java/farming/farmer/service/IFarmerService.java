package farming.farmer.service;

import java.util.List;
import java.util.Set;

import farming.farmer.dto.FarmerDto;

public interface IFarmerService {
	
	FarmerDto getFarmer(Long farmerId);
	Set<FarmerDto> getFarmersByProduct(Long productId);
	List<FarmerDto> getAllFarmers();
	
}

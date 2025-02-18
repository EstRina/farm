package farming.farmer.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farming.farmer.dto.FarmerDto;
import farming.farmer.service.IFarmerService;

@RestController
@RequestMapping("/farmer")
public class FarmerController{

	@Autowired
	IFarmerService farmerService;

	@GetMapping("/{farmerId}")
	public FarmerDto getFarmer(@PathVariable Long farmerId) {
		return farmerService.getFarmer(farmerId);
	}

	@GetMapping("/byProduct/{productId}")
	public Set<FarmerDto> getFarmersByProduct(@PathVariable Long productId) {
		return farmerService.getFarmersByProduct(productId);
	}

	@GetMapping("/all")
	public List<FarmerDto> getAllFarmers() {
		return farmerService.getAllFarmers();
	}
	
	
}

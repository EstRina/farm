package farming.products.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import farming.products.dto.ProductDto;
import farming.products.dto.RemoveProductDataDto;
import farming.products.dto.SaleRecordsDto;
import farming.products.service.IProductsService;

@RestController
@RequestMapping("/products")
public class ProductsController{
	
	@Autowired
	IProductsService productService;

	@PostMapping("/add")
    public boolean addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

	@PutMapping("/update")
	public boolean updateProduct(@RequestBody ProductDto productDto) {
		return productService.updateProduct(productDto);
	}

	@GetMapping("/{productId}")
	  public ProductDto getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }
	

	@GetMapping("/byFarmer/{farmerId}")
	public Set<ProductDto> getProductsByFarmer(@PathVariable Long farmerId) {
		return productService.getProductsByFarmer(farmerId);
	}

	@GetMapping("/priceRange/{")
	public Set<ProductDto> getProductsByPriceRange(@RequestParam double minPrice, 
			@RequestParam double maxPrice, @RequestParam Long productId) {
        return productService.getProductsByPriceRange(minPrice, maxPrice, productId);
    }

	@GetMapping("/all")
	public List<ProductDto> getAllProducts() {
		return productService.getAllProducts();
	}

	@PostMapping("/buy")
	public SaleRecordsDto buyProduct(@RequestParam Long customerId, @RequestParam Long productId, 
			@RequestParam int quantity) {
		return productService.buyProduct(customerId, productId, quantity);
	}

	@GetMapping("/sold/{farmerId}")
	public List<ProductDto> getSoldProducts(@PathVariable Long farmerId) {
		return productService.getSoldProducts(farmerId);
	}

	@GetMapping("/purchased/{customerId}")
	public List<SaleRecordsDto> getPurchasedProducts(@PathVariable Long customerId) {
		return productService.getPurchasedProducts(customerId);
	}

	@GetMapping("/history/{farmerId}")
	public List<RemoveProductDataDto> getHistoryOfRemovedProducts(@PathVariable Long farmerId) {
		return productService.getHistoryOfRemovedProducts(farmerId);
	}

	@DeleteMapping("/remove")
	public RemoveProductDataDto removeProduct(@RequestParam Long productId, @RequestParam Long farmerId) {
		return productService.removeProduct(productId, farmerId);
	}


}

package farming.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import farming.accounting.entity.UserAccount;
import farming.customer.entity.Customer;
import farming.customer.repo.CustomerRepository;
import jakarta.transaction.Transactional;

@Service
public class CustomerService implements ICustomerService{

	@Autowired
	CustomerRepository customerRepo;
	
	@Transactional
	public void createCustomerProfile(UserAccount user) {
		  Customer customer = new Customer();
	        customer.setLogin(user.getLogin());
	        customer.setFirstName(user.getFirstName());
	        customer.setLastName(user.getLastName());
	        customer.setEmail(user.getLogin());
	        customerRepo.save(customer);
	    }
	

}

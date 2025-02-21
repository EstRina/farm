package farming.accounting.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import farming.accounting.dto.UserRequestDto;
import farming.accounting.dto.UserResponseDto;
import farming.accounting.dto.UserType;
import farming.accounting.dto.exceptions.AccountActivateException;
import farming.accounting.dto.exceptions.AccountRevokeException;
import farming.accounting.dto.exceptions.PasswordValidException;
import farming.accounting.dto.exceptions.UserExistsException;
import farming.accounting.dto.exceptions.UserNotFoundException;
import farming.accounting.entity.UserAccount;
import farming.customer.service.CustomerService;
import farming.farmer.service.FarmerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountingManagement implements IAccountingManagement, CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final FarmerService farmerService; 
	private final CustomerService customerService;

	@Value("${password.length:5}")
	private int passwordLength;

	@Value("${last_hash:3}")
	private int n_last_hash;

	@Override
	@Transactional
	public UserResponseDto registration(UserRequestDto userDto, UserType userType) {
		String password = userDto.getPassword();
		String login = userDto.getLogin();
		if(!isPasswordValid(password))
			throw new PasswordValidException(password);
		
		if (userRepository.existsById(login)) {
			throw new UserExistsException(login);
		}
		UserAccount user = new UserAccount(login, passwordEncoder.encode(password), userDto.getFirstName(),
				userDto.getLastName(), userType);
		userRepository.save(user);
		if (userType == UserType.FARMER) {
	        farmerService.createFarmerProfile(user);
	    } else if (userType == UserType.CUSTOMER) {
	        customerService.createCustomerProfile(user);
	    }
		return user.build();
	}

//	public UserResponseDto registration(UserRequestDto user) {
//		String password = user.getPassword();
//		String login = user.getLogin();
//		if(!isPasswordValid(password))
//			throw new PasswordValidException(password);
//		UserAccount account = new UserAccount(login, createHash(password), user.getFirstName(), user.getLastName());
//		try {
//			template.insert(account);
//		} catch (DuplicateKeyException e) {
//			throw new UserExistsException(login);
//		}
//		return account.build();
//		
//	}
	@Override
	public UserResponseDto getUser(String login) {
		UserAccount user = getUserAccount(login);
		return user.build();
	}

	@Override
	@Transactional
	public UserResponseDto editUser(UserResponseDto user, String login) {
		UserAccount account = getUserAccount(login);
		if (user.getFirstName() != null)
			account.setFirstName(user.getFirstName());
		if (user.getLastName() != null)
			account.setFirstName(user.getLastName());
		userRepository.save(account);
		return account.build();
	}

	@Override
	@Transactional
	public boolean updatePassword(String login, String newPassword) {

		if (newPassword == null || !isPasswordValid(newPassword))
			throw new PasswordValidException(newPassword);

		UserAccount user = getUserAccount(login);

		if (passwordEncoder.matches(newPassword, user.getHash()))
			throw new PasswordValidException(newPassword);

		LinkedList<String> lastHash = user.getLastHash();
		if (isPasswordFromLast(newPassword, lastHash))
			throw new PasswordValidException(newPassword);

		if (lastHash.size() == n_last_hash)
			lastHash.removeFirst();
		lastHash.add(user.getHash());

		user.setHash(passwordEncoder.encode(newPassword));
		user.setActivationDate(LocalDateTime.now());
		userRepository.save(user);
		return true;
	}

	private boolean isPasswordValid(String password) {
		return password.length() >= passwordLength;
	}

	private boolean isPasswordFromLast(String newPassword, LinkedList<String> lastHash) {
		return lastHash.stream().anyMatch(h -> passwordEncoder.matches(newPassword, h));
	}

	@Override
	@Transactional
	public UserResponseDto removeUser(String login) {
		UserAccount user = getUserAccount(login);
		userRepository.delete(user);
		return user.build();
	}

	@Override
	@Transactional
	public boolean revokeAccount(String login) {
		UserAccount user = getUserAccount(login);
		if (user.isRevoked())
			throw new AccountRevokeException(login);
		user.setRevoked(true);
		userRepository.save(user);
		return true;
	}

	@Override
	@Transactional
	public boolean activateAccount(String login) {
		UserAccount user = getUserAccount(login);
		if (!user.isRevoked())
			throw new AccountActivateException(login);
		user.setRevoked(false);
		user.setActivationDate(LocalDateTime.now());
		userRepository.save(user);
		return true;
	}
	
	@Override
	public UserType getUserType(String login) {
	    return userRepository.findById(login)
	            .map(UserAccount::getUserType)
	            .orElseThrow(() -> new UserNotFoundException(login));
	}


	private UserAccount getUserAccount(String login) {
		UserAccount user = userRepository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		return user;
	}
	

	private String createHash(String password) {
		return passwordEncoder.encode(password);
	}

	@Override
	public String getPasswordHash(String login) {
		UserAccount user = getUserAccount(login);
		return user.getHash();
	}

	@Override
	public LocalDateTime getActivationDate(String login) {
		UserAccount user = getUserAccount(login);
		return user.getActivationDate();
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
//	@Override
//	public void run(String... args) throws Exception {
//		if(!template.exists(new Query(Criteria.where("login").is("admin")), UserAccount.class)) {
//			UserAccount admin = new UserAccount("admin", passwordEncoder.encode("admin"), "", "");
//			admin.setRoles(new HashSet<String>(List.of("ADMIN")));
//			template.save(admin);
//	}}

//	@Override
//	@Transactional
//	public void run(String... args) throws Exception {
//		if (!userRepository.existsById("admin")) {
//			UserAccount admin = new UserAccount("admin", passwordEncoder.encode("admin"), "", "");
////			admin.getRoles().add("ADMIN");
//			userRepository.save(admin);
//		}
//	}


}

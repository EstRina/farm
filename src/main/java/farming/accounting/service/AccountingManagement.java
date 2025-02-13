package farming.accounting.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import farming.accounting.dto.RolesResponseDto;
import farming.accounting.dto.UserRequestDto;
import farming.accounting.dto.UserResponseDto;
import farming.accounting.dto.exceptions.AccountActivateException;
import farming.accounting.dto.exceptions.AccountRevokeException;
import farming.accounting.dto.exceptions.PasswordValidException;
import farming.accounting.dto.exceptions.UserExistsException;
import farming.accounting.dto.exceptions.UserNotFoundException;
import farming.accounting.entity.UserAccount;

@Service
public class AccountingManagement implements IAccountingManagement, CommandLineRunner{

	@Autowired
	MongoTemplate template;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Value("${password.length:5}")
	private int passwordLength;

	@Value("${last_hash:3}")
	private int n_last_hash;

		@Override
	public UserResponseDto registration(UserRequestDto user) {
		String password = user.getPassword();
		String login = user.getLogin();
		if(!isPasswordValid(password))
			throw new PasswordValidException(password);
		UserAccount account = new UserAccount(login, createHash(password), user.getFirstName(), user.getLastName());
		try {
			template.insert(account);
		} catch (DuplicateKeyException e) {
			throw new UserExistsException(login);
		}
		return account.build();
		
	}
	
	private UserAccount getUserAccount(String login) {
		UserAccount user = template.findById(login, UserAccount.class);
		if(user==null)
			throw new UserNotFoundException(login);
		return user;
	}

	private String createHash(String password) {
		return encoder.encode(password);
	}

	private boolean isPasswordValid(String password) {
		return password.length() >= passwordLength;
	}

	@Override
	public UserResponseDto removeUser(String login) {
		Query query = new Query(Criteria.where("login").is(login));
		UserAccount user = template.findAndRemove(query, UserAccount.class);
		if(user == null)
			throw new UserNotFoundException(login);
		return user.build();
	}

	@Override
	public UserResponseDto getUser(String login) {
		UserAccount user = getUserAccount(login);
		return user.build();
	}

	@Override 
	public UserResponseDto editUser(UserResponseDto user, String login) {
		UserAccount account = getUserAccount(login);
		if(user.getFirstName() != null)
			account.setFirstName(user.getFirstName());
		if(user.getLastName() != null)
			account.setFirstName(user.getLastName());
		template.save(account);
		return account.build();
	}

	@Override
	public boolean updatePassword(String login, String newPassword) {
		
		if(newPassword == null || !isPasswordValid(newPassword))
			throw new PasswordValidException(newPassword);
		
		UserAccount user = getUserAccount(login);
		
		if(encoder.matches(newPassword, user.getHash()))
			throw new PasswordValidException(newPassword);
		
		LinkedList<String> lastHash = user.getLastHash();
		if(isPasswordFromLast(newPassword, lastHash))
			throw new PasswordValidException(newPassword);
		
		if(lastHash.size() == n_last_hash)
			lastHash.removeFirst();
		lastHash.add(user.getHash());
		
		user.setHash(encoder.encode(newPassword));
		user.setActivationDate(LocalDateTime.now());
		template.save(user);
		return true;
	}


	private boolean isPasswordFromLast(String newPassword, LinkedList<String> lastHash) {
		return lastHash.stream().anyMatch(h -> encoder.matches(newPassword, h));
	}

	@Override
	public boolean revokeAccount(String login) {
		UserAccount user = getUserAccount(login);
		if(user.isRevoked())
			throw new AccountRevokeException(login);
		user.setRevoked(true);
		template.save(user);
		return true;
	}

	@Override
	public boolean activateAccount(String login) {
		UserAccount user = getUserAccount(login);
		if(!user.isRevoked())
			throw new AccountActivateException(login);
		user.setRevoked(false);
		user.setActivationDate(LocalDateTime.now());
		template.save(user);
		return true;
	}

	@Override
	public RolesResponseDto addRole(String login, String role) {
		UserAccount user = getUserAccount(login);
		HashSet<String> roles = user.getRoles();
		if(roles.contains(role))
			throw new IllegalArgumentException("Role already exists");
		roles.add(role);
		template.save(user);
		return new RolesResponseDto(login, roles);
	}

	@Override
	public RolesResponseDto removeRole(String login, String role) {
		UserAccount user = getUserAccount(login);
		HashSet<String> roles = user.getRoles();
		if(!roles.contains(role))
			throw new IllegalArgumentException("Role doesnt exist");
		roles.remove(role);
		template.save(user);
		return new RolesResponseDto(login, roles);
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
	public RolesResponseDto getRoles(String login) {
		UserAccount user = getUserAccount(login);
		return new RolesResponseDto(login, user.getRoles());
	}

	@Override
	public void run(String... args) throws Exception {
		if(!template.exists(new Query(Criteria.where("login").is("admin")), UserAccount.class)) {
			UserAccount admin = new UserAccount("admin", encoder.encode("admin"), "", "");
			admin.setRoles(new HashSet<String>(List.of("ADMIN")));
			template.save(admin);
	}}
	

}

package farming.accounting.service;

import org.springframework.data.jpa.repository.JpaRepository;

import farming.accounting.entity.UserAccount;

public interface UserRepository extends JpaRepository<UserAccount, String>{

}

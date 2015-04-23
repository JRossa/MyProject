package org.myproject.model.repositories;



import org.myproject.model.entities.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, Long>{

	
	public PersistentLogin findBySeries(String series);
	
	public PersistentLogin findByUsername(String username);
	 
}

package jasmm.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

/*
 * Repräsentiert die Artikel Repo
 * @author Matthias & Julia
 */
@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
	
	public Shipping findByKm(Integer km);
	
}

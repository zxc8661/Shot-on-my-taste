package somt.somt.domain.address.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.domain.address.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}

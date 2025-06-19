package somt.somt.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somt.somt.user.entity.Users;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUserName(String userName);
}

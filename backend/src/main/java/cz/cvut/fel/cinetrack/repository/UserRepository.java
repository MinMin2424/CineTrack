/*
 * Created by minmin_tranova on 31.10.2025
 */

package cz.cvut.fel.cinetrack.repository;

import cz.cvut.fel.cinetrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.deleted = false AND u.username = :username")
    Optional<User> findByUsernameAndNotDeleted(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.deleted = false AND u.email = :email")
    Optional<User> findByEmailAndNotDeleted(@Param("email") String email);

}

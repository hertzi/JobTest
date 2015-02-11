package my.wf.affinitas.core.repository;

import my.wf.affinitas.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User getByEmail(String email);
    @Query("select count(u.email) + (select count(s.loginName)  from SecurityData s where s.loginName = ?1)  from User u where u.email = ?1")
    int getEmailCount(String email);
    @Query("select u from User u left join fetch u.favorites where u = ?1")
    User getUserWithFavorites(User user);
    @Query("select count(f.id) from User u join u.favorites f where u = ?1 and f=?2")
    int isFavorite(User user, User favorite);
}

package my.wf.affinitas.core.repository;

import my.wf.affinitas.core.model.SecurityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityDataRepository extends JpaRepository<SecurityData, Long>{
    SecurityData getByLoginName(String loginName);
}

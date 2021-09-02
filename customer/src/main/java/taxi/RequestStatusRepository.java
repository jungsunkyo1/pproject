package taxi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestStatusRepository extends CrudRepository<RequestStatus, Long> {


}
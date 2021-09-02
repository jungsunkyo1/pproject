package taxi;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="reciepts", path="reciepts")
public interface RecieptRepository extends PagingAndSortingRepository<Reciept, Long>{

    Optional<Reciept> findByRequestId(Long requstId);


}

package taxi;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="reciepts", path="reciepts")
public interface RecieptRepository extends PagingAndSortingRepository<Reciept, Long>{


}

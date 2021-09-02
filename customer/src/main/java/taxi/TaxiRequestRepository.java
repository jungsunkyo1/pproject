package taxi;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="taxiRequests", path="taxiRequests")
public interface TaxiRequestRepository extends PagingAndSortingRepository<TaxiRequest, Long>{


}

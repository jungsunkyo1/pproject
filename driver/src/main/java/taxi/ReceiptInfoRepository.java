package taxi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel="receiptInfos", path="receiptInfos")
public interface ReceiptInfoRepository extends CrudRepository<ReceiptInfo, Long> {


}
package taxi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel="requestAndReceiptInfos", path="requestAndReceiptInfos")
public interface RequestAndReceiptInfoRepository extends CrudRepository<RequestAndReceiptInfo, Long> {

    List<RequestAndReceiptInfo> findByRequestId(Long requestId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);
}
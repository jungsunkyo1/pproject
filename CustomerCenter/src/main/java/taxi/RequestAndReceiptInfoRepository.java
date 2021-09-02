package taxi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestAndReceiptInfoRepository extends CrudRepository<RequestAndReceiptInfo, Long> {

    List<RequestAndReceiptInfo> findByRequestId(Long requestId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);
    List<RequestAndReceiptInfo> findByRequestId(Long requestId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);
    List<RequestAndReceiptInfo> findByRecieptId(Long recieptId);

}
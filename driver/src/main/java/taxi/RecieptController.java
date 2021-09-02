package taxi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

 @RestController
 @RequestMapping("/api/reciept")
 public class RecieptController {

    @Autowired
    private RecieptRepository recieptRepository;

    @GetMapping("/acceptrequest")
    public void AcceptRequest(Long requestId, String driverName, Long driverId, String carNumber ){
        Reciept reciept = recieptRepository.findByRequestId(requestId).get();
        if(reciept != null){
            reciept.setStatus("AcceptRequest");
            reciept.setCarNumber(carNumber);
            reciept.setDriverId(driverId);
            reciept.setDriverName(driverName);
            recieptRepository.save(reciept);
        }
    }

    @GetMapping("/pickupcustomer")
    public void PickUpCustomer(Long requestId){
        Reciept reciept = recieptRepository.findByRequestId(requestId).get();
        if(reciept != null){
            reciept.setStatus("PickUpCustomer");
            recieptRepository.save(reciept);
        }
    }

    @GetMapping("/arrivedestination")
    public void ArriveDestination(Long requestId, int price){
        Reciept reciept = recieptRepository.findByRequestId(requestId).get();
        if(reciept != null){
            reciept.setStatus("ArriveDestination");
            reciept.setPrice(price);
            recieptRepository.save(reciept);
        }
    }
 }
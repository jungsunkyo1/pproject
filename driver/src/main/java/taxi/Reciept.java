package taxi;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Reciept_table")
public class Reciept {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long requestId;
    private String carNumber;
    private String driverName;
    private Integer price;
    private Long driverId;
    private String status;

    @PostPersist
    public void onPostPersist(){
        DestinationArrvied destinationArrvied = new DestinationArrvied();
        BeanUtils.copyProperties(this, destinationArrvied);
        destinationArrvied.publishAfterCommit();

        PaymentRequested paymentRequested = new PaymentRequested();
        BeanUtils.copyProperties(this, paymentRequested);
        paymentRequested.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        taxi.external.Payment payment = new taxi.external.Payment();
        // mappings goes here
        Application.applicationContext.getBean(taxi.external.PaymentService.class)
            .requestPayment(payment);

        RunningFinished runningFinished = new RunningFinished();
        BeanUtils.copyProperties(this, runningFinished);
        runningFinished.publishAfterCommit();

    }
    @PostUpdate
    public void onPostUpdate(){
        RequestAccepted requestAccepted = new RequestAccepted();
        BeanUtils.copyProperties(this, requestAccepted);
        requestAccepted.publishAfterCommit();

        CustomerPickedUp customerPickedUp = new CustomerPickedUp();
        BeanUtils.copyProperties(this, customerPickedUp);
        customerPickedUp.publishAfterCommit();

        RecieptCanceled recieptCanceled = new RecieptCanceled();
        BeanUtils.copyProperties(this, recieptCanceled);
        recieptCanceled.publishAfterCommit();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
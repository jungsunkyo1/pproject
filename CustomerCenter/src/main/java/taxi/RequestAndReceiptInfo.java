package taxi;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="RequestAndReceiptInfo_table")
public class RequestAndReceiptInfo {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Long requestId;
        private Long recieptId;
        private String startingPoint;
        private String destination;
        private Integer headcount;
        private String phoneNumber;
        private String carNumber;
        private String driverName;
        private Integer price;
        private String status;


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
        public Long getRecieptId() {
            return recieptId;
        }

        public void setRecieptId(Long recieptId) {
            this.recieptId = recieptId;
        }
        public String getStartingPoint() {
            return startingPoint;
        }

        public void setStartingPoint(String startingPoint) {
            this.startingPoint = startingPoint;
        }
        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }
        public Integer getHeadcount() {
            return headcount;
        }

        public void setHeadcount(Integer headcount) {
            this.headcount = headcount;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
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
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

}
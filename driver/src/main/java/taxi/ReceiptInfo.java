package taxi;

import javax.persistence.*;
//test
@Entity
@Table(name="ReceiptInfo_table")
public class ReceiptInfo {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String startingPoint;
        private String destinationPoint;
        private String phoneNumber;
        private Integer headcount;
        private String status;
        private Integer price;
        private String carNumber;
        private Long requestId;


        public Long getRequestId() {
            return requestId;
        }

        public void setRequestId(Long long1) {
            this.requestId = long1;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
        public String getStartingPoint() {
            return startingPoint;
        }

        public void setStartingPoint(String startingPoint) {
            this.startingPoint = startingPoint;
        }
        public String getDestinationPoint() {
            return destinationPoint;
        }

        public void setDestinationPoint(String destinationPoint) {
            this.destinationPoint = destinationPoint;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public Integer getHeadcount() {
            return headcount;
        }

        public void setHeadcount(Integer headcount) {
            this.headcount = headcount;
        }
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }
        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

}
package taxi;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="RequestStatus_table")
public class RequestStatus {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String startingPoint;
        private String destination;
        private Integer headcount;
        private String status;
        private Integer price;
        private String carNumber;
        private String driverName;


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
        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

}
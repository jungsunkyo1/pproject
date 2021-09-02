package taxi;

import taxi.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RequestStatusViewHandler {


    @Autowired
    private RequestStatusRepository requestStatusRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiRequsted_then_CREATE_1 (@Payload TaxiRequsted taxiRequsted) {
        try {

            if (!taxiRequsted.validate()) return;

            // view 객체 생성
            RequestStatus requestStatus = new RequestStatus();
            // view 객체에 이벤트의 Value 를 set 함
            requestStatus.setId(taxiRequsted.getId());
            requestStatus.setStartingPoint(taxiRequsted.getStartingPoint());
            requestStatus.setDestination(taxiRequsted.getDestination());
            requestStatus.setHeadcount(taxiRequsted.getHeadcount());
            requestStatus.setStatus("Requested");
            // view 레파지 토리에 save
            requestStatusRepository.save(requestStatus);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenRequestAccepted_then_UPDATE_1(@Payload RequestAccepted requestAccepted) {
        try {
            if (!requestAccepted.validate()) return;
                // view 객체 조회
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(requestAccepted.getRequestId());

            if( requestStatusOptional.isPresent()) {
                 RequestStatus requestStatus = requestStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 requestStatus.setStatus("Accepted");
                 requestStatus.setCarNumber(requestAccepted.getCarNumber());
                 requestStatus.setDriverName(requestAccepted.getDriverName());
                // view 레파지 토리에 save
                 requestStatusRepository.save(requestStatus);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenCustomerPickedUp_then_UPDATE_2(@Payload CustomerPickedUp customerPickedUp) {
        try {
            if (!customerPickedUp.validate()) return;
                // view 객체 조회
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(customerPickedUp.getRequestId());

            if( requestStatusOptional.isPresent()) {
                 RequestStatus requestStatus = requestStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 requestStatus.setStatus("PickedUp");
                // view 레파지 토리에 save
                 requestStatusRepository.save(requestStatus);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDestinationArrvied_then_UPDATE_3(@Payload DestinationArrvied destinationArrvied) {
        try {
            if (!destinationArrvied.validate()) return;
                // view 객체 조회
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(destinationArrvied.getRequestId());

            if( requestStatusOptional.isPresent()) {
                 RequestStatus requestStatus = requestStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 requestStatus.setStatus("Arrived");
                // view 레파지 토리에 save
                 requestStatusRepository.save(requestStatus);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRunningFinished_then_UPDATE_4(@Payload RunningFinished runningFinished) {
        try {
            if (!runningFinished.validate()) return;
                // view 객체 조회
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(runningFinished.getRequestId());

            if( requestStatusOptional.isPresent()) {
                 RequestStatus requestStatus = requestStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 requestStatus.setPrice(runningFinished.getPrice());
                 requestStatus.setStatus("Finished");
                // view 레파지 토리에 save
                 requestStatusRepository.save(requestStatus);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRequestCanceled_then_UPDATE_5(@Payload RequestCanceled requestCanceled) {
        try {
            if (!requestCanceled.validate()) return;
                // view 객체 조회
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(requestCanceled.getId());

            if( requestStatusOptional.isPresent()) {
                 RequestStatus requestStatus = requestStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 requestStatus.setStatus("Cancel Requsted");
                // view 레파지 토리에 save
                 requestStatusRepository.save(requestStatus);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRecieptCanceled_then_UPDATE_6(@Payload RecieptCanceled recieptCanceled) {
        try {
            if (!recieptCanceled.validate()) return;
                // view 객체 조회
            Optional<RequestStatus> requestStatusOptional = requestStatusRepository.findById(recieptCanceled.getRequestId());

            if( requestStatusOptional.isPresent()) {
                 RequestStatus requestStatus = requestStatusOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 requestStatus.setStatus("Canceled");
                // view 레파지 토리에 save
                 requestStatusRepository.save(requestStatus);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


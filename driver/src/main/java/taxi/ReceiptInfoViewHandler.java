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
public class ReceiptInfoViewHandler {


    @Autowired
    private ReceiptInfoRepository receiptInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiRequsted_then_CREATE_1 (@Payload TaxiRequsted taxiRequsted) {
        try {

            if (!taxiRequsted.validate()) return;

            // view 객체 생성
            ReceiptInfo receiptInfo = new ReceiptInfo();
            // view 객체에 이벤트의 Value 를 set 함
            receiptInfo.setStartingPoint(taxiRequsted.getStartingPoint());
            receiptInfo.setDestinationPoint(taxiRequsted.getDestination());
            receiptInfo.setHeadcount(taxiRequsted.getHeadcount());
            receiptInfo.setPhoneNumber(taxiRequsted.getPhoneNumber());
            receiptInfo.setStatus("Requsted");
            receiptInfo.setId(taxiRequsted.getId());
            // view 레파지 토리에 save
            receiptInfoRepository.save(receiptInfo);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenRequestAccepted_then_UPDATE_1(@Payload RequestAccepted requestAccepted) {
        try {
            if (!requestAccepted.validate()) return;
                // view 객체 조회
            Optional<ReceiptInfo> receiptInfoOptional = receiptInfoRepository.findById(requestAccepted.getId());

            if( receiptInfoOptional.isPresent()) {
                 ReceiptInfo receiptInfo = receiptInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 receiptInfo.setCarNumber(requestAccepted.getCarNumber());
                // view 레파지 토리에 save
                 receiptInfoRepository.save(receiptInfo);
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
            Optional<ReceiptInfo> receiptInfoOptional = receiptInfoRepository.findById(customerPickedUp.getId());

            if( receiptInfoOptional.isPresent()) {
                 ReceiptInfo receiptInfo = receiptInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 receiptInfo.setStatus("pickedUp");
                // view 레파지 토리에 save
                 receiptInfoRepository.save(receiptInfo);
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
            Optional<ReceiptInfo> receiptInfoOptional = receiptInfoRepository.findById(destinationArrvied.getId());

            if( receiptInfoOptional.isPresent()) {
                 ReceiptInfo receiptInfo = receiptInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 receiptInfo.setStatus("Arrived");
                // view 레파지 토리에 save
                 receiptInfoRepository.save(receiptInfo);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRecieptCanceled_then_UPDATE_4(@Payload RecieptCanceled recieptCanceled) {
        try {
            if (!recieptCanceled.validate()) return;
                // view 객체 조회
            Optional<ReceiptInfo> receiptInfoOptional = receiptInfoRepository.findById(recieptCanceled.getId());

            if( receiptInfoOptional.isPresent()) {
                 ReceiptInfo receiptInfo = receiptInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                // view 레파지 토리에 save
                 receiptInfoRepository.save(receiptInfo);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRunningFinished_then_UPDATE_5(@Payload RunningFinished runningFinished) {
        try {
            if (!runningFinished.validate()) return;
                // view 객체 조회
            Optional<ReceiptInfo> receiptInfoOptional = receiptInfoRepository.findById(runningFinished.getId());

            if( receiptInfoOptional.isPresent()) {
                 ReceiptInfo receiptInfo = receiptInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 receiptInfo.setPrice(runningFinished.getPrice());
                 receiptInfo.setStatus("Finished");
                // view 레파지 토리에 save
                 receiptInfoRepository.save(receiptInfo);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


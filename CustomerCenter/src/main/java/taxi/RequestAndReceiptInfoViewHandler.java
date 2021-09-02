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
public class RequestAndReceiptInfoViewHandler {


    @Autowired
    private RequestAndReceiptInfoRepository requestAndReceiptInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiRequsted_then_CREATE_1 (@Payload TaxiRequsted taxiRequsted) {
        try {

            if (!taxiRequsted.validate()) return;

            // view 객체 생성
            RequestAndReceiptInfo requestAndReceiptInfo = new RequestAndReceiptInfo();
            // view 객체에 이벤트의 Value 를 set 함
            requestAndReceiptInfo.setRequestId(taxiRequsted.getId());
            requestAndReceiptInfo.setStartingPoint(taxiRequsted.getStartingPoint());
            requestAndReceiptInfo.setDestination(taxiRequsted.getDestination());
            requestAndReceiptInfo.setHeadcount(taxiRequsted.getHeadcount());
            requestAndReceiptInfo.setPhoneNumber(taxiRequsted.getPhoneNumber());
            requestAndReceiptInfo.setStatus("Requested");
            // view 레파지 토리에 save
            requestAndReceiptInfoRepository.save(requestAndReceiptInfo);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenRequestAccepted_then_UPDATE_1(@Payload RequestAccepted requestAccepted) {
        try {
            if (!requestAccepted.validate()) return;
                // view 객체 조회

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRequestId(requestAccepted.getRequestId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setRecieptId(requestAccepted.getId());
                    requestAndReceiptInfo.setCarNumber(requestAccepted.getCarNumber());
                    requestAndReceiptInfo.setDriverName(requestAccepted.getDriverName());
                    requestAndReceiptInfo.setStatus("Accepted");
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
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

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRecieptId(customerPickedUp.getId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setStatus("PickedUp");
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
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

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRecieptId(destinationArrvied.getId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setStatus("Arrived");
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRequestCanceled_then_UPDATE_4(@Payload RequestCanceled requestCanceled) {
        try {
            if (!requestCanceled.validate()) return;
                // view 객체 조회

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRequestId(requestCanceled.getId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setStatus("Cancel Requested");
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentRequested_then_UPDATE_5(@Payload PaymentRequested paymentRequested) {
        try {
            if (!paymentRequested.validate()) return;
                // view 객체 조회

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRecieptId(paymentRequested.getId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setStatus("Payment Requested");
                    requestAndReceiptInfo.setPrice(paymentRequested.getPrice());
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRunningFinished_then_UPDATE_6(@Payload RunningFinished runningFinished) {
        try {
            if (!runningFinished.validate()) return;
                // view 객체 조회

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRecieptId(runningFinished.getId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setStatus("Finished");
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRecieptCanceled_then_UPDATE_7(@Payload RecieptCanceled recieptCanceled) {
        try {
            if (!recieptCanceled.validate()) return;
                // view 객체 조회

                    List<RequestAndReceiptInfo> requestAndReceiptInfoList = requestAndReceiptInfoRepository.findByRecieptId(recieptCanceled.getId());
                    for(RequestAndReceiptInfo requestAndReceiptInfo : requestAndReceiptInfoList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    requestAndReceiptInfo.setStatus("Canceled");
                // view 레파지 토리에 save
                requestAndReceiptInfoRepository.save(requestAndReceiptInfo);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


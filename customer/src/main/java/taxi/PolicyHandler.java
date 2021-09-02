package taxi;

import taxi.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired TaxiRequestRepository taxiRequestRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestAccepted_UpdateRequestStatus(@Payload RequestAccepted requestAccepted){

        if(!requestAccepted.validate()) return;

        System.out.println("\n\n##### listener UpdateRequestStatus : " + requestAccepted.toJson() + "\n\n");



        // Sample Logic //
        // TaxiRequest taxiRequest = new TaxiRequest();
        // taxiRequestRepository.save(taxiRequest);

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCustomerPickedUp_UpdateRequestStatus(@Payload CustomerPickedUp customerPickedUp){

        if(!customerPickedUp.validate()) return;

        System.out.println("\n\n##### listener UpdateRequestStatus : " + customerPickedUp.toJson() + "\n\n");



        // Sample Logic //
        // TaxiRequest taxiRequest = new TaxiRequest();
        // taxiRequestRepository.save(taxiRequest);

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDestinationArrvied_UpdateRequestStatus(@Payload DestinationArrvied destinationArrvied){

        if(!destinationArrvied.validate()) return;

        System.out.println("\n\n##### listener UpdateRequestStatus : " + destinationArrvied.toJson() + "\n\n");



        // Sample Logic //
        // TaxiRequest taxiRequest = new TaxiRequest();
        // taxiRequestRepository.save(taxiRequest);

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRunningFinished_UpdateRequestStatus(@Payload RunningFinished runningFinished){

        if(!runningFinished.validate()) return;

        System.out.println("\n\n##### listener UpdateRequestStatus : " + runningFinished.toJson() + "\n\n");



        // Sample Logic //
        // TaxiRequest taxiRequest = new TaxiRequest();
        // taxiRequestRepository.save(taxiRequest);

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRecieptCanceled_UpdateRequestStatus(@Payload RecieptCanceled recieptCanceled){

        if(!recieptCanceled.validate()) return;

        System.out.println("\n\n##### listener UpdateRequestStatus : " + recieptCanceled.toJson() + "\n\n");



        // Sample Logic //
        // TaxiRequest taxiRequest = new TaxiRequest();
        // taxiRequestRepository.save(taxiRequest);

    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
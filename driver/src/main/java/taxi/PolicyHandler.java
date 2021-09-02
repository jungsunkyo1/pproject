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
    @Autowired RecieptRepository recieptRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverTaxiRequsted_ReceiveRequest(@Payload TaxiRequsted taxiRequsted){

        if(!taxiRequsted.validate()) return;

        System.out.println("\n\n##### listener ReceiveRequest : " + taxiRequsted.toJson() + "\n\n");

        // Sample Logic //
        Reciept reciept = new Reciept();
        reciept.setRequestId(taxiRequsted.getId());
        recieptRepository.save(reciept);

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequestCanceled_CancelReciept(@Payload RequestCanceled requestCanceled){

        if(!requestCanceled.validate()) return;

        System.out.println("\n\n##### listener CancelReciept : " + requestCanceled.toJson() + "\n\n");



        // Sample Logic //
        Reciept reciept = recieptRepository.findByRequestId(requestCanceled.getId()).get();
        recieptRepository.delete(reciept);

    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
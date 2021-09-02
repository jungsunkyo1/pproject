package taxi;

public class RequestCanceled extends AbstractEvent {

    private Long id;

    public RequestCanceled(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
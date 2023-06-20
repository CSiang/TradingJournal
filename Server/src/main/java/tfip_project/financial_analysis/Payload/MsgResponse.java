package tfip_project.financial_analysis.Payload;

public class MsgResponse {
    
    private String message;

    public MsgResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MsgResponse [message=" + message + "]";
    }

    

}

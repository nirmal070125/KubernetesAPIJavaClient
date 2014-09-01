package org.apache.stratos.kubernetes.api.exceptions;

public class KubernetesClientException extends Exception {

	private static final long serialVersionUID = -7521673271244696906L;
    private String message;

    public KubernetesClientException(String message, Exception exception){
        super(message, exception);
        this.message = message;
    }

    public KubernetesClientException(Exception exception){
        super(exception);
    }
    
    public KubernetesClientException(String msg){
        super(msg);
        this.message = msg;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
}

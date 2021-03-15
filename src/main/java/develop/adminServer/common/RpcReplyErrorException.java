package develop.adminServer.common;

public class RpcReplyErrorException extends Exception {
    public RpcReplyErrorException(String errorMessage) {
        super(errorMessage);
    }

    public RpcReplyErrorException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

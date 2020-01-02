package exceptions;

public class noSuchUserException extends Exception{
    public noSuchUserException(String Message){
        super(Message);
    }
}
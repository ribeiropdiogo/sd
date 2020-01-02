package exceptions;

public class duplicateUserException extends Exception{
    
    public duplicateUserException(String Message){
        super(Message);
    }
}
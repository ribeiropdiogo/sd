import Exceptions.UserNotFound;
import Exceptions.WrongPasswordException;

public interface CloudSoundnterface {
    int register(String username, String password);
    int login(String username, String password);
}

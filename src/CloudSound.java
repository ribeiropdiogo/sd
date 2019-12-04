import Exceptions.UserNotFound;
import Exceptions.WrongPasswordException;
import Exceptions.userExists;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CloudSound {
    private HashMap<String,User> users;
    private ReentrantLock lockCloudSound;

    public CloudSound() {
        this.users = new HashMap<>();
        this.lockCloudSound = new ReentrantLock();
    }

    public int register(String username, String password) throws userExists {
        int r = 0;
        this.lockCloudSound.lock();
        User u = new User(username,password);
        if (this.users.containsKey(username)){
            r = 1;
            throw new userExists("username already exists");
        } else {
            this.users.put(username,u);
        }
        this.lockCloudSound.unlock();
        return r;
    }

    public int login(String username, String password) throws UserNotFound, WrongPasswordException {
        int r = -1;
        this.lockCloudSound.lock();

        User u = this.users.get(username);

        if (u == null){
            r = 1;
            throw new UserNotFound("> There is no user "+username);
        } else if (!u.getPassword().equals(password)){
            r = 2;
            throw new WrongPasswordException("> Wrong password");
        } else if (u.getPassword().equals(password)){
            r = 0;
            this.users.get(username).setSession(true);
        }

        this.lockCloudSound.unlock();
        return r;
    }
}

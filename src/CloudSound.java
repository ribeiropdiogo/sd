import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class CloudSound {
    private HashMap<String,User> users;
    private ReentrantLock lockCloudSound;

    public CloudSound() {
        this.users = new HashMap<>();
        this.lockCloudSound = new ReentrantLock();
    }

    public int register(String username, String password) throws userExists{
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
}

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

    public void register(String username, String password){
        this.lockCloudSound.lock();
        User u = new User(username,password);
        this.users.put(username,u);
        this.lockCloudSound.unlock();
    }
}

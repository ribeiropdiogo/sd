package model;

public class user{
    private String name;
    private String password;

    public user(String nameIn,String passwordIn){
        this.name=nameIn;
        this.password=passwordIn;
    }
    
    public String getName(){
        return this.name;
    }
    
    public boolean checkPassword(String passwordIn){
        return this.password.equals(passwordIn);
    }

}
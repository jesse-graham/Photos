package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin implements Serializable{
    public String userName = "admin";

    ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers(){
        return users;
    }

    public boolean addUser(String userName){
        for(User i : users){
            if (i.getUserName().equals(userName)){
                return false;
            }
        }
        users.add(new User(userName));
        return true;
    }

    public boolean removeUser(String userName){
        for(User i : users){
            if (i.getUserName().equals(userName)){
                users.remove(i);
                return true;
            }
        }
        return false;
    }
}
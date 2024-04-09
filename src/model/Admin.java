package model;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class Admin implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    public String userName = "admin";

    public ArrayList<User> users = new ArrayList<>();

    public static final String storeDir = "data";

    public static final String storeFile = "data.dat";

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

    public User getUser(String userName){
        for(User i : users){
            if (i.getUserName().equals(userName)){
                return i;
            }
        }
        return null;
    }

    public static void writeAdmin(Admin admin) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir+File.separator+storeFile));
        oos.writeObject(admin);
        oos.close();
    }

    public static Admin readAdmin() throws IOException, ClassNotFoundException{
        File file = new File("Data/data.dat");
        if(!file.exists()){
            return new Admin();
        }
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDir+File.separator+storeFile));
        Admin admin = (Admin) ois.readObject();
        ois.close();
        return admin;
    }
}
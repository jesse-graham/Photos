package model;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class Admin implements Serializable{
    /*
     * serialVersionUID - The constant serial number.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * userName - The username, which is always admin.
     */
    public String userName = "admin";

    /*
     * users - The list of users in the application.
     */
    public ArrayList<User> users = new ArrayList<>();

    /*
     * storeDir - The directory in which the save data is stored
     */
    public static final String storeDir = "data";

    /*
     * storeFile - The name of the file in which the save data is stored.
     */
    public static final String storeFile = "data.dat";

    /*
     * getUsers - returns the list of users.
     */
    public ArrayList<User> getUsers(){
        return users;
    }

    /*
     * addUser - adds the provided user to the list of users.
     * @param userName
     */
    public boolean addUser(String userName){
        for(User i : users){
            if (i.getUserName().equals(userName)){
                return false;
            }
        }
        users.add(new User(userName));
        return true;
    }

    /*
     * removeUser - removes the provided user from the list of users.
     * @param actionEvent
     */
    public boolean removeUser(String userName){
        for(User i : users){
            if (i.getUserName().equals(userName)){
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    /*
     * getUser - gets the user with the username corresponding with the provided username.
     * @param actionEvent
     */
    public User getUser(String userName){
        for(User i : users){
            if (i.getUserName().equals(userName)){
                return i;
            }
        }
        return null;
    }

    /*
     * writeAdmin - Saves application data to the data.dat file in the Data folder
     * @param admin
     */
    public static void writeAdmin(Admin admin) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir+File.separator+storeFile));
        oos.writeObject(admin);
        oos.close();
    }

    /*
     * readAdmin - reads saved data in the data.dat file in the Data folder.
     * @param actionEvent
     */
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
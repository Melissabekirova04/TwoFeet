package main.java.util;
import main.java.User;
import java.util.HashSet;

public class UserChecker {


    public boolean isFilledOut(String firstname, String lastname, String username, String password, String confirmPassword){
        if(firstname.isBlank() || lastname.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkLength(String input, int minimum, int maximum){
        if(input.length()<minimum || input.length() > maximum){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkForSmallLetters(String password){
        if(!password.matches("(?=.*[a-z]).*")){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkForCapitalLetters(String password){
        if(!password.matches("(?=.*[A-Z]).*")){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkForNumbers(String password){
        if(!password.matches("(?=.*[0-9]).*")){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkIfCorrectLogin(HashSet<User> users, String username, String password){
        for (User user : users) {
            if(username.equals(user.getUsername()) && password.equals((user.getPassword()))){
                    return true;
            }
        }
        return false;
    }


}


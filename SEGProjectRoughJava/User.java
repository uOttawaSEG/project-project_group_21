public class User {
    
    private String username;
    private String password;

    public User(String userName, String userPassword){
        this.username = userName;
        this.password = userPassword;
    }

    public boolean login(){
        //For front end to ask the database for user login details and such
        return false;
    }

    public void logout(){  
        //For front end to log the user out of the program
    }

}

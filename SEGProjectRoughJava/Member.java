public class Member extends User{
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Member(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber){
        super(userName,userPassword);
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.phoneNumber = userPhoneNumber;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
}

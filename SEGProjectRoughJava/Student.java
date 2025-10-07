public class Student extends Member{
    private String program;

    public Student(String userName, String userPassword, String userLastName, String userFirstName, String userPhoneNumber, String userProgram){
        super(userName,userPassword, userLastName, userFirstName, userPhoneNumber);
        program = userProgram;
    }

    public String getProgram(){
        return program;
    }

    public String registerUser(){
        //For back end to save the STUDENT user i think
        return null;
    }
    
}

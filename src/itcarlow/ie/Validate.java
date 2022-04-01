package itcarlow.ie;

public class Validate {

    // validate inputted email
    // using java.util.regex.patterns.match()
    public static boolean validEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        // return true if pattern matches regex
        return email.matches(regex);
    }

    // validate inputted password
    // using java.util.regex.patterns.match()
    // 1 number, 1 a-z, 1 A-Z, 1 special char, no white space, at least 8 characters
    public static boolean validPassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        // return true if it matches the regex pattern
        return password.matches(regex);
    }
}

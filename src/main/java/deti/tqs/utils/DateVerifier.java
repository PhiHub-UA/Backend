package deti.tqs.utils;

public class DateVerifier {

    public boolean isDateValid(String date) {
        if (date == null) {
            return false;
        }
        return date.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})");
    }
    
}

package deti.tqs.utils;

public class DateVerifier {

    public boolean isDateValid(String date) {
        if (date == null) {
            return false;
        }
        return date.matches("(\\d{2})-(\\d{2})-(\\d{4})");
    }
    
}


package deti.tqs.phihub.models;
import java.util.ArrayList;
import java.util.List;

public enum Speciality {
    CARDIOLOGY(0),
    DERMATOLOGY(1),
    ENDOCRINOLOGY(2),
    GASTROENTROLOGY(3),
    GYNECOLOGY(4),
    HEMATOLOGY(5),
    NEUROLOGY(6),
    PHTHALMOLOGY(7),
    OTORHINOLARYNGOLOGY(8),
    PEDIATRICS(9),
    PSYCHIATRY(10),
    PULMONOLOGY(11),
    RHEUMATOLOGY(12),
    UROLOGY(13);

    private final int id;

    Speciality(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Speciality fromId(int id) {
        for (Speciality speciality : Speciality.values()) {
            if (speciality.getId() == id) {
                return speciality;
            }
        }
        return null;
    }

    public static Speciality fromString(String speciality) {
        for (Speciality s : Speciality.values()) {
            if (s.toString().equalsIgnoreCase(speciality)) {
                return s;
            }
        }
        return null;
    }

    public static List<Speciality> fromStrings(List<String> specialities) {
        List<Speciality> specialityList = new ArrayList<>();
        for (String speciality : specialities) {    
            specialityList.add(fromString(speciality));
        }
        return specialityList;
    }

}

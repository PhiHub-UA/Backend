package deti.tqs.phihub.models;

import java.util.List;
import java.util.ArrayList;

public enum StaffPermissions {

    CREATE(0),
    MANAGE(1),
    RECEPTION(2);

    private final int id;

    StaffPermissions(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static StaffPermissions fromId(int id) {
        for (StaffPermissions staffPermission : StaffPermissions.values()) {
            if (staffPermission.getId() == id) {
                return staffPermission;
            }
        }
        return null;
    }

    public static List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        for (StaffPermissions staffPermission : StaffPermissions.values()) {
            permissions.add(staffPermission.toString());
        }
        return permissions;
    }

    public static StaffPermissions fromString(String permission) {

        for (StaffPermissions staffPermission : StaffPermissions.values()) {
            if (staffPermission.toString().equalsIgnoreCase(permission)) {
                return staffPermission;
            }
        }
        return null;
    }

    public static List<StaffPermissions> fromStrings(List<String> permissions) {
        List<StaffPermissions> specialityList = new ArrayList<>();

        for (String permission : permissions) {
            specialityList.add(fromString(permission));
        }

        return specialityList;

    }

}

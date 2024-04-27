package deti.tqs.phihub.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Info
    private String name;
    private String phone;
    private String email;
    private Integer age;
    // Login Info
    private String username;
    private String password;
    private String role;

    public User(String name, String phone, String email, Integer age, String username, String password,
            String role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.username = username;
        this.password = password;
        this.role = role;
        this.bills = new ArrayList<>();
        this.oldAppointments = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role.equals("admin")) {
            return List.of(new SimpleGrantedAuthority("admin"), new SimpleGrantedAuthority("user"));
        }
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @OneToMany
    private List<Bill> bills;

    @OneToMany
    private List<Appointment> oldAppointments;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

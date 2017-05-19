package vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Vehicle {

    @JsonIgnore
    public String vin;

    @Id
    @GeneratedValue
    private Long id;

    Vehicle() {
    }

    public Vehicle(String vin) {
        this.vin = vin;
    }

    @OneToMany(mappedBy = "vehicle")
    private Set<Recalls> recalls = new HashSet<>();

    public Set<Recalls> getRecalls() {
        return recalls;
    }

    public Long getId() {
        return id;
    }

    public String getVin() {
        return vin;
    }


}

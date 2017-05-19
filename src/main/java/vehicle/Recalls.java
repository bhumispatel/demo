package vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Recalls {
    @JsonIgnore
    @ManyToOne
    private Vehicle vehicle;

    @Id
    @GeneratedValue
    private Long id;
    private String description;


    Recalls() {
    }

    public Recalls(Vehicle vehicle, String des) {
        this.vehicle = vehicle;
        this.description = des;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

}

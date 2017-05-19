package vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RecallsRepository extends JpaRepository<Recalls, Long> {
    Collection<Recalls> findByVehicleVin(String vin);
}

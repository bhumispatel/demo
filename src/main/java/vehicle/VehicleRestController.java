package vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/recall")
public class VehicleRestController {

    private final VehicleRepository vehicleRepository;

    private final RecallsRepository recallsRepository;

    @Autowired
    public VehicleRestController(VehicleRepository v, RecallsRepository r) {
        vehicleRepository = v;
        recallsRepository = r;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{vin}")
    Collection<Recalls> getRecalls(@PathVariable String vin) {
        return this.recallsRepository.findByVehicleVin(vin);
    }
}

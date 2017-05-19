package vehicle;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(VehicleRepository vehicleRepository, RecallsRepository recallsRepository) {
        return (evt) -> Arrays.asList("123abc,345dfg,678xyz".split(","))
                        .forEach(
                                a -> {
                                    Vehicle vehicle = vehicleRepository.save(new Vehicle(a));
                                    recallsRepository.save(new Recalls(vehicle, "test recall 1 on " + vehicle.getVin()));
                                    recallsRepository.save(new Recalls(vehicle, "test recall 2 on " + vehicle.getVin()));
                                }
                        );
    }
}

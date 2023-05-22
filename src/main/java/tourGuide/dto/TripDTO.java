package tourGuide.dto;

import lombok.Data;
import org.javamoney.moneta.Money;

@Data
public class TripDTO {
    private int attractionProximity;
    private int tripDuration;
    private int numberOfAdults;
    private int numberOfChildren;
    private int ticketQuantity;
    private String currency;
    private Money lowerPricePoint;
    private Money highPricePoint;
}

package tourGuide.dto;

import org.javamoney.moneta.Money;

public class TripDTO {
    private int locationProximity;
    private int tripDuration;
    private int numberOfAdults;
    private int numberOfChildren;
    private int ticketQuantity;
    private String currency;
    private Money lowerPricePoint;
    private Money highPricePoint;

    /** Getters / Setters */
    public int getLocationProximity() {
        return locationProximity;
    }
    public void setLocationProximity(int locationProximity) {
        this.locationProximity = locationProximity;
    }

    public int getTripDuration() {
        return tripDuration;
    }
    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }
    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }
    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }
    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Money getLowerPricePoint() {
        return lowerPricePoint;
    }
    public void setLowerPricePoint(Money lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public Money getHighPricePoint() {
        return highPricePoint;
    }
    public void setHighPricePoint(Money highPricePoint) {
        this.highPricePoint = highPricePoint;
    }
}

package tourGuide.dto;

import gpsUtil.location.Location;
import lombok.Data;

@Data
public class AttractionDTO {
    private String name;
    private Location coordinates;
    private int rewardPoints;
}

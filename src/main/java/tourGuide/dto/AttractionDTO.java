package tourGuide.dto;

import gpsUtil.location.Location;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AttractionDTO {
    private String attractionName;
    private Location attractionCoordinates;
    private Location userCoordinates;
    private Double distance;
    private int rewardPoints;
}

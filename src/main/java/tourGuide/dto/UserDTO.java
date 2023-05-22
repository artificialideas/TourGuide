package tourGuide.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private TripDTO tripDetails;
}

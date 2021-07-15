package com.light.location.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    private long id;

    private Long city;

    private Long state;

    private Long country;

    private String pincode;

    private String gpsLocation;

    public Address(Long city, Long state, Long country, String pincode) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

}

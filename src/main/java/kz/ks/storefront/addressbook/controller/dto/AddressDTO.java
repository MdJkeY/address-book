package kz.ks.storefront.addressbook.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.ks.storefront.addressbook.enums.CoordinateSystem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO {
    @JsonProperty("city_id")
    private String cityId;
    @JsonProperty("street_name")
    private String streetName;
    private String house;
    private String apartment;

    private double lat;
    private double lon;
    @JsonProperty("coordinate_system")
    private CoordinateSystem cs;

    private boolean visible;

    @JsonProperty("customer_gci")
    private String customerGci;
}

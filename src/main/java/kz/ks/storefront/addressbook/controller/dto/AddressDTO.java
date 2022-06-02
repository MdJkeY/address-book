package kz.ks.storefront.addressbook.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(value = "geo_point_dto", required = false)
    private GeoPointDTO geoPointDTO;

    private boolean visible;

    @JsonProperty("customer_gci")
    private String customerGci;
}

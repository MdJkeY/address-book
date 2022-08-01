package kz.ks.storefront.addressbook.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PersistentAddressDTO", description = "POJO with database id field. ")
public class PersistentAddressDTO {
    @Schema(
            description = "Database ID",
            example = "47896"
    )
    private Long id;

    @JsonProperty("city_id")
    @Schema(
            description = "City ID",
            example = "48"
    )
    private String cityId;

    @JsonProperty("street_name")
    @Schema(
            description = "Street name",
            example = "Abay"
    )
    private String streetName;

    @Schema(
            description = "House",
            example = "12"
    )
    private String house;

    @Schema(
            description = "Apartment",
            example = "65B"
    )
    private String apartment;

    @JsonProperty(value = "geo_point_dto")
    @Schema(
            description = "Geographical Point (Data Transfer Object)"
    )
    private GeoPointDTO geoPointDTO;

    @Schema(
            description = "Address visibility flag",
            example = "true"
    )
    private boolean visible;


    @JsonProperty("customer_gci")
    @Schema(
            description = "Global Customer ID",
            example = "1374"
    )
    private String customerGci;
}

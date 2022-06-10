package kz.ks.storefront.addressbook.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class GeoPointDTO {
    @Schema(
            description = "Latitude",
            example = "89.12"
    )
    private double lat;

    @Schema(
            description = "Longitude",
            example = "76.93"
    )
    private double lon;

    @JsonProperty("coordinate_system")
    @Schema(
            description = "Coordinate System",
            example = "WGS84"
    )
    private CoordinateSystem coordinateSystem;
}

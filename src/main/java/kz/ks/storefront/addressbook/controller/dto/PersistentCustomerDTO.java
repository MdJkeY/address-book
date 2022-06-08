package kz.ks.storefront.addressbook.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistentCustomerDTO {
    @Schema(
            description = "Customer ID",
            example = "87098664732"
    )
    private long id;

    @Schema(
            description = "Global Customer ID",
            example = "9761245"
    )
    private String gci;
}

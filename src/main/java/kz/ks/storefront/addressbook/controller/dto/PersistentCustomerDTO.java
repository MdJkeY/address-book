package kz.ks.storefront.addressbook.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistentCustomerDTO {
    private long id;
    private String gci;
}

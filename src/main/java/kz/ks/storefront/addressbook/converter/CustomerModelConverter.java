package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.CustomerDTO;
import kz.ks.storefront.addressbook.model.CustomerModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerModelConverter implements Converter<CustomerModel, CustomerDTO> {
    @Override
    public CustomerDTO convert(CustomerModel source) {
        return CustomerDTO.builder()
                .gci(source.getGci())
                .build();
    }
}

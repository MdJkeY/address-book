package kz.ks.storefront.addressbook.converter;

import kz.ks.storefront.addressbook.controller.dto.PersistentCustomerDTO;
import kz.ks.storefront.addressbook.model.CustomerModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerModelPersistentConverter implements Converter<CustomerModel, PersistentCustomerDTO> {
    @Override
    public PersistentCustomerDTO convert(CustomerModel source) {
        return PersistentCustomerDTO.builder()
                .id(source.getId())
                .gci(source.getGci())
                .build();
    }
}

package org.example.mapper;


import org.example.DTO.CustomerDTO;
import org.example.Entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "productIds", expression = "java(customer.getProducts().stream().map(p -> p.getId()).collect(java.util.stream.Collectors.toList()))")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "products", ignore = true)
    Customer fromDTO(CustomerDTO customerDTO);
}


package org.example.mapper;

import org.example.dto.ProductDTO;
import org.example.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "customer.id", target = "customerId") // Преобразуем ссылку на ID покупателя
    ProductDTO toDTO(Product product);

    @Mapping(target = "customer", ignore = true) // Связь с покупателем будет заполняться отдельно
    Product fromDTO(ProductDTO productDTO);
}

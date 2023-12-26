package com.SecurityAplication.AutenticationApp.DTO;

import java.math.BigDecimal;

public record ProductDTO(Long id, Long  cod,String productName, BigDecimal productValue, Long quantity) {
}

package br.com.leandroauler.orders.api.service;

import br.com.leandroauler.orders.api.entity.Products;
import br.com.leandroauler.orders.api.entity.dto.ProductDTO;
import br.com.leandroauler.orders.api.exception.ProductNotFoundException;
import br.com.leandroauler.orders.api.repository.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductsRepository productRepository;

    public ProductService(ProductsRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Products createProduct(ProductDTO productDTO) {
        Products products = new Products();
        products.setName(productDTO.getName());
        products.setDescription(productDTO.getDescription());
        products.setPrice(productDTO.getPrice());
        products.setCreatedAt(LocalDateTime.now());

        return productRepository.save(products);
    }

    public List<Products> findAllProducts() {
        return productRepository.findAll();
    }

    @Cacheable(value = "productCache", key = "#productId")
    public Products findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto com ID " + productId + " não encontrado."));
    }


}

package br.com.leandroauler.process.orders.service;


import br.com.leandroauler.process.orders.entity.ItemOrder;
import br.com.leandroauler.process.orders.entity.Product;
import br.com.leandroauler.process.orders.entity.dto.ProductDTO;
import br.com.leandroauler.process.orders.exception.ProductNotFoundException;
import br.com.leandroauler.process.orders.repository.ProductRepository;
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

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(List<ItemOrder> itens) {

        itens.forEach(item -> {
            productRepository.save(item.getProduct());
        });
    }

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Cacheable(value = "productCache", key = "#productId")
    public Product findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto com ID " + productId + " não encontrado."));
    }


}

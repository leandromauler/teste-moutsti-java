package br.com.leandroauler.orders.api.service;

import br.com.leandroauler.orders.api.entity.Products;
import br.com.leandroauler.orders.api.entity.dto.ProductDTO;
import br.com.leandroauler.orders.api.exception.ProductNotFoundException;
import br.com.leandroauler.orders.api.repository.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductsRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Products products;
    private ProductDTO productDTO;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productId = UUID.randomUUID();

        // Inicializa o Product
        products = new Products();
        products.setId(productId);
        products.setName("Test Product");
        products.setDescription("Test Description");
        products.setPrice(100.0);
        products.setCreatedAt(LocalDateTime.now());

        // Inicializa o ProductDTO
        productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(100.0);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        when(productRepository.save(any(Products.class))).thenReturn(products);

        // Act
        Products result = productService.createProduct(productDTO);

        // Assert
        assertEquals(products.getName(), result.getName());
        assertEquals(products.getDescription(), result.getDescription());
        assertEquals(products.getPrice(), result.getPrice());
        verify(productRepository, times(1)).save(any(Products.class));
    }

    @Test
    void testFindAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(products));

        // Act
        List<Products> products = productService.findAllProducts();

        // Assert
        assertEquals(1, products.size());
        Products result = products.get(0);
        assertEquals(products.get(0).getId(), result.getId());
        assertEquals(products.get(0).getName(), result.getName());
        assertEquals(products.get(0).getDescription(), result.getDescription());
        assertEquals(products.get(0).getPrice(), result.getPrice());
        assertEquals(products.get(0).getCreatedAt(), result.getCreatedAt());
        assertEquals(products.get(0).getUpdatedAt(), result.getUpdatedAt());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ProductExists() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(products));

        // Act
        Products result = productService.findById(productId);

        // Assert
        assertEquals(products, result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testFindById_ProductNotFound() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
        verify(productRepository, times(1)).findById(productId);
    }
}


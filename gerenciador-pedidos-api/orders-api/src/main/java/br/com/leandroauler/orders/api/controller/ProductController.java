package br.com.leandroauler.orders.api.controller;

import br.com.leandroauler.orders.api.entity.Order;
import br.com.leandroauler.orders.api.entity.Product;
import br.com.leandroauler.orders.api.entity.Products;
import br.com.leandroauler.orders.api.entity.dto.ProductDTO;
import br.com.leandroauler.orders.api.service.OrderService;
import br.com.leandroauler.orders.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Produtos", description = "Recurso para manipulação dos produtos")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Operation(summary = "Criar um produto",responses = @ApiResponse(responseCode = "201", description = "Produto criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))))
    @PostMapping("/create")
    public ResponseEntity<Products> createProduct(@RequestBody ProductDTO productDTO) {
        logger.info("Criando produto: {}", productDTO.toString());
        Products createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Buscar todos os produtos",responses = @ApiResponse(responseCode = "200", description = "buscar todos os produtos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))))
    @GetMapping
    public ResponseEntity<List<Products>> findAll() {
        List<Products> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }


    // Endpoint para buscar um produto pelo ID
    @Operation(summary = "Buscar um produto por ID",responses = @ApiResponse(responseCode = "200", description = "buscar Produto por ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))))
    @GetMapping("/{productId}")
    public ResponseEntity<Products> findById(@PathVariable UUID productId) {
        Products products = productService.findById(productId);
        return ResponseEntity.ok(products);
    }
}

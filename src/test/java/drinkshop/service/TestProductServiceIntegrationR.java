package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestProductServiceIntegrationR {

    @TempDir
    Path tempDir;

    @Test
    void addProduct_validProduct_realValidatorAndRealRepository_productIsSaved() throws IOException{
        Path filePath = tempDir.resolve("products2.txt");
        Files.createFile(filePath);

        Repository<Integer, Product> productRepo =
                new FileProductRepository(filePath.toString());

        Validator<Product> productValidator = new ProductValidator();

        ProductService service = new ProductService(productRepo, productValidator);

        Product p = new Product(
                1,
                "Cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        service.addProduct(p);

        List<Product> products = service.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Cola", products.get(0).getNume());
        assertEquals(10.0, products.get(0).getPret());
    }

    @Test
    void addProduct_invalidProduct_realValidatorAndRealRepository_productIsNotSaved() throws IOException {
        Path filePath = tempDir.resolve("products2.txt");
        Files.createFile(filePath);

        Repository<Integer, Product> productRepo =
                new FileProductRepository(filePath.toString());

        Validator<Product> productValidator = new ProductValidator();

        ProductService service = new ProductService(productRepo, productValidator);

        Product p = new Product(
                -1,
                "Pepsi",
                5.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(p));

        assertTrue(service.getAllProducts().isEmpty());
    }
}
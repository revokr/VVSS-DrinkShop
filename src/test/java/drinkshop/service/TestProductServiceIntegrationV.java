package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TestProductServiceIntegrationV {

    @Test
    void addProduct_validProduct_realValidator_savesProduct() {
        Repository<Integer, Product> productRepo = mock(Repository.class);
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

        verify(productRepo).save(p);
    }

    @Test
    void addProduct_invalidProduct_realValidator_doesNotSaveProduct() {
        Repository<Integer, Product> productRepo = mock(Repository.class);
        Validator<Product> productValidator = new ProductValidator();

        ProductService service = new ProductService(productRepo, productValidator);

        Product p = new Product(
                -1,
                "Cola",
                5.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(p));

        verify(productRepo, never()).save(any(Product.class));
    }
}
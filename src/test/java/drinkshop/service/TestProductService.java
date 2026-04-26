package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestProductService {

    @Mock
    private Repository<Integer, Product> productRepo;

    @Mock
    private Validator<Product> productValidator;

    @Test
    void addProduct_validProduct_callsValidatorAndRepositorySave() {
        ProductService service = new ProductService(productRepo, productValidator);

        Product p = new Product(
                1,
                "Cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        service.addProduct(p);

        verify(productValidator).validate(p);
        verify(productRepo).save(p);
    }

    @Test
    void addProduct_invalidProductFromValidator_doesNotSaveProduct() {
        ProductService service = new ProductService(productRepo, productValidator);

        Product p = new Product(
                1,
                "Cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        doThrow(new ValidationException("Produs invalid"))
                .when(productValidator)
                .validate(p);

        assertThrows(ValidationException.class, () -> service.addProduct(p));

        verify(productValidator).validate(p);
        verify(productRepo, never()).save(any(Product.class));
    }

    @Test
    void addProduct_nullProduct_throwsException() {
        ProductService service = new ProductService(productRepo, productValidator);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(null));

        verify(productValidator, never()).validate(any());
        verify(productRepo, never()).save(any());
    }
}
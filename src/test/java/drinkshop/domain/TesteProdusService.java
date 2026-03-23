package drinkshop.domain;

import drinkshop.repository.Repository;
import drinkshop.repository.file.FileOrderRepository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.repository.file.FileRetetaRepository;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.DrinkShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Teste addProduct folosind ECP si BVA")
class TesteProdusService {

    private DrinkShopService service;

    @BeforeEach
    void setUp() {
        // ---------- Initializare Repository-uri care citesc din fisiere ----------
        Repository<Integer, Product> productRepo = new FileProductRepository("data/products.txt");
        Repository<Integer, drinkshop.domain.Order> orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
        Repository<Integer, Reteta> retetaRepo = new FileRetetaRepository("data/retete.txt");
        Repository<Integer, Stoc> stocRepo = new FileStocRepository("data/stocuri.txt");

        // ---------- Initializare Service ----------
        service = new DrinkShopService(productRepo, orderRepo, retetaRepo, stocRepo);
    }

    private Product createProduct(String nume, double pret) {
        return new Product(
                1,
                nume,
                pret,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );
    }

    @Test
    @Order(1)
    @Tag("ECP")
    @DisplayName("ECP valid: nume valid si pret valid")
    void addProduct_ECP_valid() {
        Product p = createProduct("Cola", 10);

        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @Order(2)
    @Tag("ECP")
    @DisplayName("ECP invalid: nume null")
    void addProduct_ECP_invalid_numeNull() {
        Product p = createProduct(null, 10);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }

    @Test
    @Order(3)
    @Tag("ECP")
    @DisplayName("ECP invalid: nume gol")
    void addProduct_ECP_invalid_numeGol() {
        Product p = createProduct("", 10);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }

    @Test
    @Order(4)
    @Tag("ECP")
    @DisplayName("ECP invalid: pret sub limita")
    void addProduct_ECP_invalid_pretSubLimita() {
        Product p = createProduct("Cola", 0);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }

    @Test
    @Order(5)
    @Tag("ECP")
    @DisplayName("ECP invalid: pret peste limita")
    void addProduct_ECP_invalid_pretPesteLimita() {
        Product p = createProduct("Cola", 130);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }

    @Test
    @Order(6)
    @Tag("BVA")
    @DisplayName("BVA valid: nume lungime minima valida")
    void addProduct_BVA_valid_numeMinim() {
        Product p = createProduct("C", 10);

        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @Order(7)
    @Tag("BVA")
    @DisplayName("BVA invalid: nume sub limita minima")
    void addProduct_BVA_invalid_numeSubMinim() {
        Product p = createProduct("", 10);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }

    @Test
    @Order(8)
    @Tag("BVA")
    @DisplayName("BVA valid: pret la limita minima")
    void addProduct_BVA_valid_pretMinim() {
        Product p = createProduct("Fanta", 1);

        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @Order(9)
    @Tag("BVA")
    @DisplayName("BVA invalid: pret imediat sub limita minima")
    void addProduct_BVA_invalid_pretSubMinim() {
        Product p = createProduct("Fanta", 0);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }

    @Test
    @Order(10)
    @Tag("BVA")
    @DisplayName("BVA valid: pret la limita maxima")
    void addProduct_BVA_valid_pretMaxim() {
        Product p = createProduct("Sprite", 129);

        assertDoesNotThrow(() -> service.addProduct(p));
    }

    @Test
    @Order(11)
    @Tag("BVA")
    @DisplayName("BVA invalid: pret imediat peste limita maxima")
    void addProduct_BVA_invalid_pretPesteMaxim() {
        Product p = createProduct("Sprite", 130);

        assertThrows(IllegalArgumentException.class, () -> service.addProduct(p));
    }
}
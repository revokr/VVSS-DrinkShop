package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileOrderRepository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.repository.file.FileRetetaRepository;
import drinkshop.repository.file.FileStocRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Teste computeTotal")
class TesteOrderService {

    private DrinkShopService service;
    private Repository<Integer, Product> productRepo;
    private Repository<Integer, drinkshop.domain.Order> orderRepo;
    private Repository<Integer, Reteta> retetaRepo;
    private Repository<Integer, Stoc> stocRepo;

    @BeforeEach
    void setUp() {
        // ---------- Initializare Repository-uri care citesc din fisiere ----------
        productRepo = new FileProductRepository("data/products.txt");
        orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
        retetaRepo = new FileRetetaRepository("data/retete.txt");
        stocRepo = new FileStocRepository("data/stocuri.txt");

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

    private drinkshop.domain.Order createOrderWithItems(List<OrderItem> items) {
        drinkshop.domain.Order order = new drinkshop.domain.Order();
        order.setItems(items);
        return order;
    }

    private OrderItem createOrderItem(Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("F02_TC01 - computeTotal(null) returns 0")
    void F02_TC01_computeTotal_orderNull_returnsZero() {
        double total = service.computeTotal(null);

        assertEquals(0.0, total);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("F02_TC02 - computeTotal(order with empty items) returns 0")
    void F02_TC02_computeTotal_emptyItems_returnsZero() {
        drinkshop.domain.Order order = createOrderWithItems(Collections.emptyList());

        double total = service.computeTotal(order);

        assertEquals(0.0, total);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("F02_TC04 - computeTotal(order with missing product in repo) returns 0")
    void F02_TC04_computeTotal_productNotFound_returnsZero() {
        Product productRef = new Product(
                9999,
                "Ghost",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        OrderItem item = createOrderItem(productRef, 1);
        drinkshop.domain.Order order = createOrderWithItems(List.of(item));

        double total = service.computeTotal(order);

        assertEquals(0.0, total);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("F02_TC05 - computeTotal(valid order) returns correct total")
    void F02_TC05_computeTotal_validOrder_returnsCorrectTotal() {
        Product product = productRepo.findOne(1);

        OrderItem item = createOrderItem(product, 1);
        drinkshop.domain.Order order = createOrderWithItems(List.of(item));

        double total = service.computeTotal(order);

        assertEquals(19.0, total);
    }


}
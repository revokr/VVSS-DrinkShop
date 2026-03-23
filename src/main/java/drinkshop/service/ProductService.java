package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;

    public ProductService(Repository<Integer, Product> productRepo) {
        this.productRepo = productRepo;
    }

    public void addProduct(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("Produsul nu poate fi null.");
        }

        if (p.getNume() == null) {
            throw new IllegalArgumentException("Numele produsului nu poate fi null.");
        }

        if (p.getNume().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele produsului nu poate fi gol.");
        }

        if (p.getNume().length() > 50) {
            throw new IllegalArgumentException("Numele produsului este prea lung.");
        }

        if (p.getPret() < 1 || p.getPret() > 129) {
            throw new IllegalArgumentException("Pretul produsului trebuie sa fie in intervalul [1, 129].");
        }

        productRepo.save(p);
    }

    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        Product updated = new Product(id, name, price, categorie, tip);
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
//        Iterable<Product> it=productRepo.findAll();
//        ArrayList<Product> products=new ArrayList<>();
//        it.forEach(products::add);
//        return products;

//        return StreamSupport.stream(productRepo.findAll().spliterator(), false)
//                    .collect(Collectors.toList());
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(CategorieBautura categorie) {
        if (categorie == CategorieBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getCategorie() == categorie)
                .collect(Collectors.toList());
    }

    public List<Product> filterByTip(TipBautura tip) {
        if (tip == TipBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getTip() == tip)
                .collect(Collectors.toList());
    }
}
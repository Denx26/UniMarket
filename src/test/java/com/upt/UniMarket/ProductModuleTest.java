package com.upt.UniMarket;

import com.upt.UniMarket.Controllers.ProductController;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ProductModuleTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private Product makeProduct(Long pid, Long vanzatorId, String nume, Float pret, Boolean negociabil, Float pretMin) {
        Product p = new Product();
        p.setPid(pid);
        p.setVanzatorId(vanzatorId);
        p.setNume(nume);
        p.setPret(pret);
        p.setNegociabil(negociabil);
        p.setPretMin(pretMin);
        p.setDescriere("Test description");
        return p;
    }

    @Test
    void addProduct_savesProductToRepository() {
        Product product = makeProduct(null, 1L, "Textbook", 50.0f, false, null);

        productController.addProduct(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void editProduct_setsPidFromPathVariable() {
        Product product = makeProduct(null, 1L, "Textbook", 50.0f, false, null);

        productController.editProduct(product, 5L);

        assertEquals(5L, product.getPid());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void editProduct_overridesBodyPidWithPathVariable() {
        Product product = makeProduct(99L, 1L, "Chair", 200.0f, false, null);

        productController.editProduct(product, 7L);

        assertEquals(7L, product.getPid());
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_callsDeleteByIdWithCorrectId() {
        productController.deleteProduct(3L);

        verify(productRepository, times(1)).deleteById(3L);
    }

    @Test
    void findByVanzatorId_returnsSellerProducts() {
        List<Product> products = List.of(
                makeProduct(1L, 2L, "Laptop", 1500.0f, true, 1200.0f),
                makeProduct(2L, 2L, "Mouse", 80.0f, false, null)
        );
        when(productRepository.findByVanzatorId(2L)).thenReturn(products);

        List<Product> result = productController.findByVanzatorId(2L);

        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getNume());
        assertEquals("Mouse", result.get(1).getNume());
    }

    @Test
    void findByVanzatorId_returnsEmptyList_whenSellerHasNoProducts() {
        when(productRepository.findByVanzatorId(99L)).thenReturn(List.of());

        List<Product> result = productController.findByVanzatorId(99L);

        assertEquals(0, result.size());
        verify(productRepository, times(1)).findByVanzatorId(99L);
    }
}

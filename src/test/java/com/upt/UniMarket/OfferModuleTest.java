package com.upt.UniMarket;

import com.upt.UniMarket.Dto.OfferRequest;
import com.upt.UniMarket.Entity.Offer;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Repositories.OfferRepository;
import com.upt.UniMarket.Repositories.ProductRepository;
import com.upt.UniMarket.Services.OfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferModuleTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OfferService offerService;

    private Product makeProduct(Long pid, Boolean negociabil, Float pretMin) {
        Product p = new Product();
        p.setPid(pid);
        p.setNume("Laptop Test");
        p.setPret(5000.0f);
        p.setNegociabil(negociabil);
        p.setPretMin(pretMin);
        p.setVanzatorId(1L);
        return p;
    }

    private OfferRequest makeOfferRequest(Long productId, Double proposedPrice) {
        OfferRequest req = new OfferRequest();
        req.setProductId(productId);
        req.setBuyerId(2L);
        req.setProposedPrice(proposedPrice);
        return req;
    }


    @Test
    void createOffer_savesOffer_whenValid() throws Exception {
        Product product = makeProduct(1L, true, 4500.0f);
        OfferRequest request = makeOfferRequest(1L, 4800.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Offer result = offerService.createOffer(request);

        assertNotNull(result);
        assertEquals(4800.0f, result.getProposedPrice());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void createOffer_throwsException_whenProductNotNegotiable() {
        Product product = makeProduct(1L, false, null);
        OfferRequest request = makeOfferRequest(1L, 4800.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(Exception.class, () -> {
            offerService.createOffer(request);
        });

        assertEquals("This product is not negotiable", exception.getMessage());
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    void createOffer_throwsException_whenPriceIsUnderMinimum() {
        Product product = makeProduct(1L, true, 4500.0f);
        OfferRequest request = makeOfferRequest(1L, 4200.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(Exception.class, () -> {
            offerService.createOffer(request);
        });

        assertEquals("The proposed price is under the minimum margin", exception.getMessage());
        verify(offerRepository, never()).save(any(Offer.class));
    }


    @Test
    void getOfferByProductId_returnsList() {
        List<Offer> mockOffers = List.of(new Offer(1L, 2L, 4800.0));
        when(offerRepository.findByProductId(1L)).thenReturn(mockOffers);

        List<Offer> result = offerService.getOfferByProductId(1L);

        assertEquals(1, result.size());
        verify(offerRepository, times(1)).findByProductId(1L);
    }

    @Test
    void approveOffer_deletesProductAndOffers() throws Exception {
        Offer mockOffer = new Offer(1L, 2L, 4800.0);
        mockOffer.setId(10L);

        when(offerRepository.findById(10L)).thenReturn(Optional.of(mockOffer));

        offerService.approveOffer(10L);

        verify(offerRepository, times(1)).deleteByProductId(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.models.Bill;
import deti.tqs.phihub.repositories.BillRepository;
import deti.tqs.phihub.services.BillService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BillServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    private Bill bill0 = new Bill();
    private Bill bill1 = new Bill();

    @BeforeEach
    public void setUp() {
        //  Create two bills
        bill0.setId(1L);
        bill0.setPrice(12.3);
        bill0.setDate(new Date());

        bill1.setId(2L);
        bill1.setPrice(23.7);
        bill1.setDate(new Date());

        List<Bill> allBills = Arrays.asList(bill0, bill1);

        Mockito.when(billRepository.save(bill0)).thenReturn(bill0);
        Mockito.when(billRepository.save(bill1)).thenReturn(bill1);
        Mockito.when(billRepository.findAll()).thenReturn(allBills);
        Mockito.when(billRepository.findById(bill0.getId())).thenReturn(Optional.of(bill0));
        Mockito.when(billRepository.findById(bill1.getId())).thenReturn(Optional.of(bill1));
        Mockito.when(billRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
     void whenSaveValidBill_thenBillShouldBeReturned() {
        Bill returned = billService.save(bill0);
        assertThat(returned.getPrice()).isEqualTo(bill0.getPrice());

        returned = billService.save(bill1);
        assertThat(returned.getPrice()).isEqualTo(bill1.getPrice());
    }

    @Test
     void whenSearchValidID_thenBillshouldBeFound() {
        Bill found = billService.getBillById(bill0.getId());
        assertThat(found.getPrice()).isEqualTo(bill0.getPrice());

        found = billService.getBillById(bill1.getId());
        assertThat(found.getPrice()).isEqualTo(bill1.getPrice());
    }

    @Test
     void whenSearchInvalidID_thenBillShouldNotBeFound() {
        Bill fromDb = billService.getBillById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(billRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void given2Bills_whengetAll_thenReturn2Records() {

        List<Bill> allBills = billService.findAll();

        assertThat(allBills).hasSize(2).extracting(Bill::getPrice).contains(bill0.getPrice(), bill1.getPrice());

        Mockito.verify(billRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }
}

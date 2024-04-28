package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.Bill;
import deti.tqs.phihub.repositories.BillRepository;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class BillRepositoryTests {

    @Autowired
    private BillRepository billRepository;

    private Bill bill0 = new Bill();
    private Bill bill1 = new Bill();

    @BeforeAll
    public void setUp() throws Exception {

        bill0.setId(1L);
        bill0.setPrice(12.3);
        bill0.setDate(new Date());
        
        billRepository.saveAndFlush(bill0);

        bill1.setId(2L);
        bill1.setPrice(25.7);
        bill1.setDate(new Date());
    }

    @Test
    void whenFindBillById_thenReturnBill() {

        Bill found = billRepository.findById(bill0.getId()).get();
        
        assertThat(found.getPrice()).isEqualTo(bill0.getPrice());
        assertThat(found.getDate().getTime()).isEqualTo(bill0.getDate().getTime());
    }

    @Test
    void whenInvalidBillId_thenReturnNull() {
        Bill found = billRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenBillIsDeleted_thenReturnNull() {
        billRepository.delete(bill0);

        Bill found = billRepository.findById(bill0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfBills_whenFindAll_thenReturnAllBills() {
        //  Save the bill 1
        billRepository.saveAndFlush(bill1);

        List<Bill> allBills = billRepository.findAll();

        assertThat(allBills).hasSize(2).extracting(Bill::getPrice).containsOnly(bill0.getPrice(), bill1.getPrice());
    }
}
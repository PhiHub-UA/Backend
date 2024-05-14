package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.repositories.QueueLineRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class QueueLineRepositoryTests {

    @Autowired
    private QueueLineRepository queuelineRepository;

    private QueueLine queue0 = new QueueLine();
    private QueueLine queue1 = new QueueLine();

    @BeforeAll
    public void setUp() {

        queue0.setId(1L);
        queue0.setMaxSize(12);
        
        queuelineRepository.saveAndFlush(queue0);

        queue1.setId(2L);
        queue1.setMaxSize(16);
    }

    @Test
    void whenFindQueueLineById_thenReturnQueueLine() {

        QueueLine found = queuelineRepository.findById(queue0.getId()).get();
        
        assertThat(found.getMaxSize()).isEqualTo(queue0.getMaxSize());
    }

    @Test
    void whenInvalidQueueLineId_thenReturnNull() {
        QueueLine found = queuelineRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenQueueLineIsDeleted_thenReturnNull() {
        queuelineRepository.delete(queue0);

        QueueLine found = queuelineRepository.findById(queue0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfQueueLines_whenFindAll_thenReturnAllQueueLines() {
        //  Save the queueline 1
        queuelineRepository.saveAndFlush(queue1);

        List<QueueLine> allQueueLines = queuelineRepository.findAll();

        assertThat(allQueueLines).hasSize(2).extracting(QueueLine::getMaxSize).containsOnly(queue0.getMaxSize(), queue1.getMaxSize());
    }
}
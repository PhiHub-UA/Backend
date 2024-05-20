package deti.tqs.phihub.defaults;

import org.springframework.boot.ApplicationRunner;

import deti.tqs.phihub.configs.Generated;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.repositories.QueueLineRepository;


@Component
@Generated
public class QueueLines implements ApplicationRunner {

    QueueLineRepository queueLineRepository;

    public QueueLines(QueueLineRepository queueLineRepository) {
        this.queueLineRepository = queueLineRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (queueLineRepository.count() > 0) {
            return;
        }

        QueueLine queue1 = new QueueLine();
        queue1.setMaxSize(200);
        queue1.setShowingLetter("A");


        QueueLine queue2 = new QueueLine();
        queue2.setMaxSize(200);
        queue2.setShowingLetter("B");

        QueueLine queue3 = new QueueLine();
        queue3.setMaxSize(200);
        queue3.setShowingLetter("C");

        QueueLine queue4 = new QueueLine();
        queue4.setMaxSize(200);
        queue4.setShowingLetter("P");

        queueLineRepository.save(queue1);
        queueLineRepository.save(queue2);
        queueLineRepository.save(queue3);
        queueLineRepository.save(queue4);

        System.out.println("Queue Lines created");



    }

}

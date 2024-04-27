package deti.tqs.phihub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.WaitingRoom;

@Repository
public interface WaitingRoomRepository extends JpaRepository<WaitingRoom, Long> {

}

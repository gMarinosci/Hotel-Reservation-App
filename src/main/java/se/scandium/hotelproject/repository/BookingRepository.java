package se.scandium.hotelproject.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.scandium.hotelproject.dto.RoomDto;
import se.scandium.hotelproject.entity.Booking;
import se.scandium.hotelproject.entity.Room;
import se.scandium.hotelproject.entity.RoomType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Integer> {

    // Basic CRUD

    @Modifying(clearAutomatically = true)
    @Query("UPDATE  Booking b SET b.pay = :pay WHERE b.id = :id")
    void updatePaymentStatus(@Param("id") int id, @Param("pay") boolean pay);

    List<Booking> findAllByRoom_IdAndStatusFalse(int roomId);

    //@Modifying(clearAutomatically = true)
    //@Query("select b from Booking b where b.room.id = :roomId and b.status = false and b.toDate > :fromDate") // 2020-01-02 > 2020-01-04
    List<Booking> findAllByStatusFalseAndToDateGreaterThanAndRoomId(LocalDate fromDate, int roomId);

    List<Booking> findAllByStatusFalseAndFromDateGreaterThanEqualAndToDateLessThan(LocalDate fromDate, LocalDate toDate);

    ///List <Booking> findAllBySpecificBookedDay(LocalDate checkDay);

    List<Booking> findAllByStatusFalseAndToDateGreaterThan(LocalDate date);

    List<Booking> findAllByRoom_NameAndStatusFalse(String roomName);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE Booking b SET b.status = :status WHERE b.id = :id")
    void updateStatusByBookingId(@Param("id") int id, @Param("status") boolean status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Booking b SET b.fromDate = null, b.toDate = null WHERE b.id = :id and b.status = false")
    void resetBookingDate(@Param("id") int id);

//    @Query("select distinct b.Booking")
//    SELECT * FROM Booking
//    WHERE NOT (From_date > @RangeTill OR To_date < @RangeFrom)

//    @Modifying(clearAutomatically = true)
//    @Query("select distinct Booking from Booking b where not (( b.fromDate > :checkDay) and (b.toDate < :checkDay))")

    List<Booking> findAllByFromDateIsLessThanEqualAndToDateIsGreaterThanEqual(LocalDate checkDate, LocalDate checkDate2);

    @Modifying(clearAutomatically = true)
    @Query("select distinct b from Booking b where :lastName = b.customer.lastName")
    List<Booking> findBookingByLastName(@Param("lastName") String lastName);

    @Modifying(clearAutomatically = true)
    @Query("select distinct b.room from Booking b where b.room.type = :roomType and ((b.fromDate < :toDate) and (b.toDate > :fromDate))")
    List<Room> findAllUnavailableRooms(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("roomType")RoomType roomType);
}

///@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate
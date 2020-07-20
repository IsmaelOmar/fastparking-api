package com.fastparking.api.database.hibernate.entity;

import org.joda.time.DateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "t_bookings")
public class BookingEntity implements Serializable {

    private UUID bookingId;
    private DateTime bookingFromTime;
    private DateTime bookingToTime;
    private UUID userId;
    private UUID parkingBayId;
    private ParkingBaysEntity parkingBaysEntity;

    @Id
    @Column(name = "booking_id", nullable = false)
    public UUID getBookingId() {
        return bookingId;
    }

    @Column(name = "booking_from_time", nullable = false)
    public DateTime getBookingFromTime() {
        return bookingFromTime;
    }

    @Column(name = "booking_to_time", nullable = false)
    public DateTime getBookingToTime() {
        return bookingToTime;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public UUID getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "parking_spot_id", nullable = false)
    public UUID getParkingBayId() {
        return parkingBayId;
    }

    @OneToOne
    public ParkingBaysEntity getParkingBaysEntity() {
        return parkingBaysEntity;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public void setBookingFromTime(DateTime bookingFromTime) {
        this.bookingFromTime = bookingFromTime;
    }

    public void setBookingToTime(DateTime bookingToTime) {
        this.bookingToTime = bookingToTime;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setParkingBayId(UUID parkingBayId) {
        this.parkingBayId = parkingBayId;
    }

    public void setParkingBaysEntity(ParkingBaysEntity parkingBaysEntity) {
        this.parkingBaysEntity = parkingBaysEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookingEntity that = (BookingEntity) o;
        return Objects.equals(bookingId, that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}

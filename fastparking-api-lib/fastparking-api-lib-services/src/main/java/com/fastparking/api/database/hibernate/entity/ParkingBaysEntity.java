package com.fastparking.api.database.hibernate.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "t_parkingbays")
public class ParkingBaysEntity implements Serializable {

    private UUID parkingBayId;
    private Integer parkingFloor;
    private String parkingBayNumber;
    private String parkingSite;
    private Boolean available;

    @Id
    @Column(name = "parking_bay_id", nullable = false)
    public UUID getParkingBayId() {
        return parkingBayId;
    }

    @Basic
    @Column(name = "parking_floor", nullable = false)
    public Integer getParkingFloor() {
        return parkingFloor;
    }

    @Basic
    @Column(name = "parking_bay_number", nullable = false)
    public String getParkingBayNumber() {
        return parkingBayNumber;
    }

    @Basic
    @Column(name = "parking_site", nullable = false)
    public String getParkingSite() {
        return parkingSite;
    }

    @Basic
    @Column(name = "available", nullable = false)
    public Boolean getAvailable() {
        return available;
    }

    public void setParkingBayId(UUID parkingBayId) {
        this.parkingBayId = parkingBayId;
    }

    public void setParkingFloor(Integer parkingFloor) {
        this.parkingFloor = parkingFloor;
    }

    public void setParkingBayNumber(String parkingBayNumber) {
        this.parkingBayNumber = parkingBayNumber;
    }

    public void setParkingSite(String parkingSite) {
        this.parkingSite = parkingSite;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingBaysEntity that = (ParkingBaysEntity) o;
        return Objects.equals(parkingBayId, that.parkingBayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingBayId);
    }
}

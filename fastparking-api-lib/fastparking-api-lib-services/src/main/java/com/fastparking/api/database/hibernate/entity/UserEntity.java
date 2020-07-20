package com.fastparking.api.database.hibernate.entity;

import org.joda.time.DateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "t_users")
public class UserEntity implements Serializable {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String licensePlate;
    private String email;
    private Date dateOfSignUp;
    private UUID userLoginId;
    private UserLoginEntity userLoginEntity;
    private Set<BookingEntity> bookingEntitySet;

    @Id
    @Column(name = "user_id", nullable = false)
    public UUID getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "firstname", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Basic
    @Column(name = "lastname", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "licenseplate", nullable = false)
    public String getLicensePlate() {
        return licensePlate;
    }

    @Basic
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    @Column(name = "dateOfSignUp", nullable = false)
    public Date getDateOfSignUp() {
        return dateOfSignUp;
    }

    @Basic
    @Column(name = "user_login_id", nullable = false)
    public UUID getUserLoginId() {
        return userLoginId;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_login_id", insertable = false, updatable = false)
    public UserLoginEntity getUserLoginEntity() {
        return userLoginEntity;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userId")
    public Set<BookingEntity> getBookingEntitySet() {
        return bookingEntitySet;
    }

    public void setUserLoginId(UUID userLoginId) {
        this.userLoginId = userLoginId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfSignUp(Date dateOfSignUp) {
        this.dateOfSignUp = dateOfSignUp;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserLoginEntity(UserLoginEntity userLoginEntity) {
        this.userLoginEntity = userLoginEntity;
    }

    public void setBookingEntitySet(Set<BookingEntity> bookingEntitySet) {
        this.bookingEntitySet = bookingEntitySet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

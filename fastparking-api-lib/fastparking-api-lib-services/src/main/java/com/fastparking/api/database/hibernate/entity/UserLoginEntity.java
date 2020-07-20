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
@Table(name = "t_users_login")
public class UserLoginEntity implements Serializable {

    private UUID userLoginId;
    private String username;
    private String password;
    private boolean signedIn;

    @Id
    @Column(name = "u_uid", nullable = false)
    public UUID getUserLoginId() {
        return userLoginId;
    }

    @Basic
    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }

    @Basic
    @Column(name = "user_password", nullable = false)
    public String getPassword() {
        return password;
    }

    @Basic
    @Column(name = "signed_in", nullable = false)
    public boolean isSignedIn() {
        return signedIn;
    }

    public void setUserLoginId(UUID uid) {
        this.userLoginId = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLoginEntity that = (UserLoginEntity) o;
        return Objects.equals(userLoginId, that.userLoginId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLoginId);
    }
}

package com.triple.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Where(clause = "deleted = false")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private boolean deleted;

    @Column(nullable = false, unique = true)
    private String accountId;

    protected User() {

    }

    public User(String accountId) {
        this.deleted = false;
        this.accountId = accountId;
    }

    public UUID getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isDeleted() == user.isDeleted() &&
                Objects.equals(getId(), user.getId()) &&
                Objects.equals(getAccountId(), user.getAccountId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isDeleted(), getAccountId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}

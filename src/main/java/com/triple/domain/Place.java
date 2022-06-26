package com.triple.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Place extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private User user;

    protected Place() {

    }

    public static Place emptyPlace(UUID id, User user) {
        return new Place(id, user);
    }

    public Place(String address, String name, User user) {
        this.address = address;
        this.name = name;
        this.content = "너무 좋아요 힐링하고 왔습니다.";
        this.deleted = false;
        this.user = user;
    }

    private Place(UUID id, User user) {
        this.id = id;
        this.address = "삭제된 장소";
        this.name = "삭제된 장소";
        this.content = "삭제된 장소";
        this.deleted = false;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return isDeleted() == place.isDeleted() && Objects.equals(getId(), place.getId()) &&
                Objects.equals(getAddress(), place.getAddress()) && Objects.equals(getName(), place.getName()) &&
                Objects.equals(getContent(), place.getContent()) && Objects.equals(getUser(), place.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAddress(), getName(), getContent(), isDeleted(), getUser());
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", user=" + user +
                '}';
    }
}

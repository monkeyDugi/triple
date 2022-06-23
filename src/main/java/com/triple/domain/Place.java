package com.triple.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Where(clause = "deleted = false")
@Entity
public class Place extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

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

    public Place(User user) {
        this.name = "바람의 언덕";
        this.content = "너무 좋아요 힐링하고 왔습니다.";
        this.deleted = false;
        this.user = user;
    }

    public Place(UUID id, User user) {
        this.id = id;
        this.name = "바람의 언덕";
        this.content = "너무 좋아요 힐링하고 왔습니다.";
        this.deleted = false;
        this.user = user;
    }

    public UUID getId() {
        return id;
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
                Objects.equals(getName(), place.getName()) && Objects.equals(getContent(), place.getContent()) &&
                Objects.equals(getUser(), place.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getContent(), isDeleted(), getUser());
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", user=" + user +
                '}';
    }
}

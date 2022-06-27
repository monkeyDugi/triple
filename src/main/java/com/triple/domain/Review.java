package com.triple.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PLACE_ID", nullable = false)
    private Place place;

    protected Review() {

    }

    public static Review emptyReview(UUID id, User user, Place place) {
        return new Review(id, user, place);
    }

    public Review(User user, Place place) {
        this.content = "꼭 가야하는 곳이에요.";
        this.deleted = false;
        this.user = user;
        this.place = place;
    }

    private Review(UUID id, User user, Place place) {
        this.id = id;
        this.content = "삭제된 리뷰";
        this.deleted = true;
        this.user = user;
        this.place = place;
    }

    public UUID getId() {
        return id;
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

    public Place getPlace() {
        return place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return isDeleted() == review.isDeleted() && Objects.equals(getId(), review.getId()) &&
                Objects.equals(getContent(), review.getContent()) && Objects.equals(getUser(), review.getUser()) &&
                Objects.equals(getPlace(), review.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), isDeleted(), getUser(), getPlace());
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", user=" + user +
                ", place=" + place +
                '}';
    }
}

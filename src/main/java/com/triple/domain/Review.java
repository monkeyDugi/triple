package com.triple.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Lob
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PLACE_ID", nullable = false)
    private Place place;

    protected Review() {

    }

    public Review(UUID id, User user, Place place) {
        this.id = id;
        this.content = "꼭 가야하는 곳이에요.";
        this.user = user;
        this.place = place;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
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
        return Objects.equals(id, review.id) &&
                Objects.equals(content, review.content) &&
                Objects.equals(user, review.user) &&
                Objects.equals(place, review.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, user, place);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", place=" + place +
                '}';
    }
}

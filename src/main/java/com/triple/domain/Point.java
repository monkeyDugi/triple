package com.triple.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Point extends BaseTimeEntity {
    @Id
//    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String score;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public UUID getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(getId(), point.getId()) && Objects.equals(getScore(), point.getScore()) && Objects.equals(getUser(), point.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getScore(), getUser());
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", score='" + score + '\'' +
                ", user=" + user +
                '}';
    }
}

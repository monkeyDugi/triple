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
public class Point extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    protected Point() {

    }

    public Point(int score, User user) {
        this.score = score;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public int getScore() {
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
        return getScore() == point.getScore() &&
                Objects.equals(getId(), point.getId()) &&
                Objects.equals(getUser(), point.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getScore(), getUser());
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", score=" + score +
                ", user=" + user +
                '}';
    }
}

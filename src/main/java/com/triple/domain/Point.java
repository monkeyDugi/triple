package com.triple.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
    @Type(type = "uuid-char")
    private UUID id;

    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    protected Point() {

    }

    public static Point emptyPoint(UUID userId) {
        return new Point(null, User.emptyUser(userId));
    }

    public Point(User user) {
        this.user = user;
    }

    private Point(UUID id, User user) {
        this.id = id;
        this.score = 0;
        this.user = user;
    }

    public void updateScore(int score) {
        this.score += score;
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

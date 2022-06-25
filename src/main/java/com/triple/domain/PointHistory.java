package com.triple.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class PointHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int photoCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private Review review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PLACE_ID", nullable = false)
    private Place place;

    protected PointHistory() {

    }

    public static PointHistory emptyPointHistory() {
        return new PointHistory(0, null, null, 0, null, null, null);
    }

    public PointHistory(int score, ActionType action, String content, int photoCount, Review review, User user, Place place) {
        this.score = score;
        this.action = action;
        this.content = content;
        this.photoCount = photoCount;
        this.review = review;
        this.user = user;
        this.place = place;
    }

    public boolean isFirstReview() {
        return action == ActionType.DELETE || action == null;
    }

    public UUID getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public ActionType getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public Review getReview() {
        return review;
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
        PointHistory that = (PointHistory) o;
        return getScore() == that.getScore() && getPhotoCount() == that.getPhotoCount() &&
                Objects.equals(getId(), that.getId()) && getAction() == that.getAction() &&
                Objects.equals(getContent(), that.getContent()) && Objects.equals(getReview(), that.getReview()) &&
                Objects.equals(getUser(), that.getUser()) && Objects.equals(getPlace(), that.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getScore(), getAction(), getContent(), getPhotoCount(), getReview(), getUser(), getPlace());
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "id=" + id +
                ", score=" + score +
                ", action=" + action +
                ", content='" + content + '\'' +
                ", photoCount=" + photoCount +
                ", review=" + review +
                ", user=" + user +
                ", place=" + place +
                '}';
    }
}

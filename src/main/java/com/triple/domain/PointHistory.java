package com.triple.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

public class PointHistory extends BaseTimeEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String score;

    @Column(nullable = false)
    private String action;

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

    public UUID getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public String getAction() {
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
        return getPhotoCount() == that.getPhotoCount() &&
                Objects.equals(getId(), that.getId()) && Objects.equals(getScore(), that.getScore()) &&
                Objects.equals(getAction(), that.getAction()) && Objects.equals(getContent(), that.getContent()) &&
                Objects.equals(getReview(), that.getReview()) && Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getPlace(), that.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getScore(), getAction(), getContent(), getPhotoCount(), getReview(), getUser(), getPlace());
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "id=" + id +
                ", score='" + score + '\'' +
                ", action='" + action + '\'' +
                ", content='" + content + '\'' +
                ", photoCount=" + photoCount +
                ", review=" + review +
                ", user=" + user +
                ", place=" + place +
                '}';
    }
}

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
public class Photo extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false, unique = false)
    private String storeFileName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private Review review;

    protected Photo() {

    }

    public Photo(String originFileName, String storeFileName, Review review) {
        this.originFileName = originFileName;
        this.storeFileName = storeFileName;
        this.review = review;
    }

    public UUID getId() {
        return id;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public String getStoreFileName() {
        return storeFileName;
    }

    public Review getReview() {
        return review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(getId(), photo.getId()) &&
                Objects.equals(getOriginFileName(), photo.getOriginFileName()) &&
                Objects.equals(getStoreFileName(), photo.getStoreFileName()) &&
                Objects.equals(getReview(), photo.getReview());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOriginFileName(), getStoreFileName(), getReview());
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", originFileName='" + originFileName + '\'' +
                ", storeFileName='" + storeFileName + '\'' +
                ", review=" + review +
                '}';
    }
}


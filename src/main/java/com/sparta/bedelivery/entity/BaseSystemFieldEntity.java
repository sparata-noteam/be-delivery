package com.sparta.bedelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseSystemFieldEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @CreatedBy
    @Column(nullable = false)
    private String createBy;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @LastModifiedBy
    @Column(nullable = false)
    private String updateBy;

    @Column
    private LocalDateTime deleteAt;

    @Column
    private String deleteBy;

    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }
    // 소프트 삭제 처리
    public void delete(String deletedBy) {
        this.deleteBy = deletedBy;
        this.deleteAt = LocalDateTime.now();
    }

}

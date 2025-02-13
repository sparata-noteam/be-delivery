package com.sparta.bedelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)

@MappedSuperclass
@Getter
@Setter
public abstract class BaseSystemFieldEntity {

    @CreatedDate
    private LocalDateTime createAt;

    @Column
    private String createBy;

    @LastModifiedBy
    private LocalDateTime updateAt;

    @Column
    private String updateBy;

    @Column
    private LocalDateTime deleteAt;

    @Column
    private String deleteBy;
}

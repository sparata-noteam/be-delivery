package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

//@EntityListeners(AuditingEntityListener.class) //UserAuditorAware 사용 삭제
@MappedSuperclass
@Getter
@Setter
public abstract class BaseSystemFieldEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createBy;

//    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updateAt;

//    @LastModifiedBy
    @Column(nullable = true)
    private String updateBy;

    @Column
    private LocalDateTime deleteAt;

    @Column
    private String deleteBy;

    // CREATE 시에는 updateAt과 updateBy를 설정하지 않음
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createAt == null) {
            createAt = now;
        }
        if (createBy == null) {
            createBy =  getCurrentUsername();  // 실제 사용자 정보
        }
        updateAt = null;
        updateBy = null;
        // CREATE 시에는 updateAt과 updateBy를 설정하지 않음
    }

    // UPDATE 시에만 updateAt과 updateBy를 갱신
    @PreUpdate
    public void preUpdate() {
        if (updateAt == null) {
            updateAt = LocalDateTime.now();
        }
        if (updateBy == null) {
            updateBy =  getCurrentUsername();  // 실제 사용자 정보
        }
    }
    // 소프트 삭제 처리
    public void delete(String deletedBy) {
        this.deleteBy = deletedBy;
        this.deleteAt = LocalDateTime.now();
    }

    // 현재 인증된 사용자의 이름을 가져오는 메서드
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();  // 로그인한 사용자의 이름 반환
        }
        return "anonymousUser";  // 인증되지 않은 경우 'anonymous' 반환
    }

}

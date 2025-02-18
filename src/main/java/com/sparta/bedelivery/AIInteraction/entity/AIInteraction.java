package com.sparta.bedelivery.AIInteraction.entity;

import com.sparta.bedelivery.entity.BaseSystemFieldEntity;
import com.sparta.bedelivery.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_ai_interactions")
public class AIInteraction extends BaseSystemFieldEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String queryText;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseText;

    public AIInteraction(User user, String queryText, String responseText){
        this.user=user;
        this.queryText=queryText;
        this.responseText=responseText;
    }

}


package com.example.team_service.jpa;

import com.example.team_service.dto.TeamDto;
import com.example.team_service.enums.TeamStatus;
import com.example.team_service.vo.InsertTeam;
import com.example.team_service.vo.UpdateTeam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "teams")
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_team_id")
    private Team upperTeam;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Team(InsertTeam insertTeam, Team upperTeam) {
        this.name = insertTeam.getName();;
        this.upperTeam = upperTeam;
        this.status =  TeamStatus.valueOf(insertTeam.getStatus());
    }

    public Team(InsertTeam insertTeam) {
        this.name = insertTeam.getName();;
        this.status =  TeamStatus.valueOf(insertTeam.getStatus());
    }

    public void updateTeam(UpdateTeam updateTeam) {
        if (StringUtils.hasText(updateTeam.getName())) {
            this.name = updateTeam.getName();
        }

        if (upperTeam.getUpperTeam() != null) {
            this.upperTeam = upperTeam.getUpperTeam();
        }

        if (StringUtils.hasText(updateTeam.getStatus())) {
            this.status = TeamStatus.valueOf(updateTeam.getStatus());
        }
    }
}

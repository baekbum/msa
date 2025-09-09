package com.example.user_service.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCond {
    Long id;
    String name;
    Long upperTeamId;
    String upperTeamName;
    String status;
    List<Long> teamIdList;
    List<String> teamNameList;
    List<Long> upperTeamIdList;
    List<String> upperTeamNameList;
}

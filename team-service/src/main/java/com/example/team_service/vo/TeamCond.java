package com.example.team_service.vo;

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

    Integer page = 0; // page 필드에 기본값 0 할당
    Integer size = 10; // size 필드에 기본값 10 할당

    // sort 필드를 List<String>으로 변경합니다.
    List<String> sort; // "sort": ["teamName-asc","createdAt-desc"] 예시

    public boolean isParamExist() {
        return this.id != null
                || this.name != null
                || this.upperTeamId != null
                || this.upperTeamName != null
                || this.status != null
                || (this.teamIdList != null && !this.teamIdList.isEmpty())
                || (this.teamNameList != null && !this.teamNameList.isEmpty())
                || (this.upperTeamIdList != null && !this.upperTeamIdList.isEmpty())
                || (this.upperTeamNameList != null && !this.upperTeamNameList.isEmpty());
    }
}

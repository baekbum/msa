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
public class UserCond {
    Long id;
    String email;
    String name;
    String userId;
    List<String> userIdList;

    Long teamId;
    List<Long> teamIdList;

    Integer page = 0; // page 필드에 기본값 0 할당
    Integer size = 10; // size 필드에 기본값 10 할당

    // sort 필드를 List<String>으로 변경합니다.
    List<String> sort; // "sort": ["teamName-asc","createdAt-desc"] 예시

    public boolean isParamExist() {
        return this.id != null
                || this.email != null
                || this.userId != null
                || (this.userIdList != null && !this.userIdList.isEmpty())
                || this.teamId != null
                || (this.teamIdList != null && !this.teamIdList.isEmpty());
    }
}

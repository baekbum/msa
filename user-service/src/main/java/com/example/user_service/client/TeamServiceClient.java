package com.example.user_service.client;

import com.example.user_service.dto.TeamDto;
import com.example.user_service.vo.TeamCond;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "team-service")
public interface TeamServiceClient {

    @GetMapping("/exist/{teamId}")
    ResponseEntity<Void> isExistTeam(@PathVariable("teamId") Long teamId);

    @GetMapping("/search/{teamId}")
    ResponseEntity<TeamDto> selectById(@PathVariable("teamId") Long teamId);

    @PostMapping("/search")
    ResponseEntity<Page<TeamDto>> selectByCond(@RequestBody TeamCond cond);
}

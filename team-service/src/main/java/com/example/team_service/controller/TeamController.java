package com.example.team_service.controller;

import com.example.team_service.dto.TeamDto;
import com.example.team_service.service.TeamService;
import com.example.team_service.vo.InsertTeam;
import com.example.team_service.vo.TeamCond;
import com.example.team_service.vo.UpdateTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/add")
    public ResponseEntity<?> insert(@RequestBody InsertTeam insertInfo) {
        log.info("Team add process start");
        log.info("InsertInfo : {}", insertInfo.toString());

        TeamDto insertDto = teamService.insert(insertInfo);
        log.info("InsertDto : {}", insertDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(insertDto);
    }

    @GetMapping("/exist/{teamId}")
    public ResponseEntity<?> isExist(@PathVariable("teamId") Long teamId) {
        log.info("Team exist process start");
        log.info("TeamId : {}", teamId);
        HttpStatus httpStatus = teamService.existById(teamId) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).build();
    }

    @GetMapping("/search/{teamId}")
    public ResponseEntity<?> select(@PathVariable("teamId") Long teamId) {
        log.info("Team search process start");
        log.info("TeamId : {}", teamId);

        TeamDto selectDto = teamService.selectById(teamId);
        log.info("SelectDto : {}", selectDto);

        return ResponseEntity.status(HttpStatus.OK).body(selectDto);
    }

    @PostMapping("/search")
    public ResponseEntity<?> selectByCond(@RequestBody(required = false) TeamCond cond) {
        log.info("Team search process with cond start");
        log.info("TeamCond : {}", cond);

        Page<TeamDto> selectDtoList = cond.isParamExist() ? teamService.selectByCond(cond) : teamService.selectAll(cond);
        log.info("selectDtoList size : {}", selectDtoList.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(selectDtoList);
    }

    @PostMapping("/update/{teamId}")
    public ResponseEntity<?> update(@PathVariable("teamId") Long teamId, @RequestBody(required = false) UpdateTeam updateInfo) {
        log.info("Team update process start");
        log.info("UpdateInfo : {}", updateInfo);

        TeamDto updateDto = teamService.update(teamId, updateInfo);
        log.info("UpdateDto : {}", updateDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateDto);
    }

    @PostMapping("/delete/{teamId}")
    public ResponseEntity<?> delete(@PathVariable("teamId") Long teamId) {
        log.info("Team search process start");
        log.info("TeamId : {}", teamId);

        TeamDto deleteDto = teamService.delete(teamId);
        log.info("DeleteDto : {}", deleteDto);

        return ResponseEntity.status(HttpStatus.OK).body(deleteDto);
    }

}

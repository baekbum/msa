package com.example.user_service.service;

import com.example.user_service.client.TeamServiceClient;
import com.example.user_service.dto.TeamDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.exception.TeamNotExistException;
import com.example.user_service.jpa.User;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.TeamCond;
import com.example.user_service.vo.UpdateUser;
import com.example.user_service.vo.UserCond;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static feign.FeignException.*;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository repository;
    private final TeamServiceClient teamServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    /**
     * 유저 추가 메서드
     * @param insertInfo
     * @return
     */
    public UserDto insert(InsertUser insertInfo) {
        try {
            UserDto userDto = new UserDto(repository.insert(insertInfo));
            TeamDto teamDto = getTeamInfo(userDto.getTeamId());
            userDto.setTeamName(teamDto.getName());

            return userDto;
        } catch (FeignClientException.BadRequest ex) {
            throw new TeamNotExistException("해당 팀은 존재하지 않습니다.", ex);
        }
    }

    /**
     * 유저 존재 확인 메서드
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Boolean existById(String userId) {
        UserDto userDto = new UserDto(repository.selectById(userId));
        return StringUtils.hasText(userDto.getUserId());
    }

    /**
     * 전체 유저 조회 메서드
     * @return
     */
    @Transactional(readOnly = true)
    public List<UserDto> selectAll() {
        List<User> users = repository.selectAll();
        HashMap<Long, String> teamMap = teamDtoListToMap(getTeamsInfo(extractTeamId(users)));

        return makeUserDtoList(users, teamMap);
    }

    /**
     * 유저 ID로 검색 메서드
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public UserDto selectById(String userId) {
        UserDto userDto = new UserDto(repository.selectById(userId));
        TeamDto teamDto = getTeamInfo(userDto.getTeamId());
        userDto.setTeamName(teamDto.getName());

        return userDto;
    }

    /**
     * 유저 조건으로 검색 메서드
     * @param cond
     * @return
     */
    @Transactional(readOnly = true)
    public List<UserDto> selectByCond(UserCond cond) {
        List<User> users = repository.selectByCond(cond);
        HashMap<Long, String> teamMap = teamDtoListToMap(getTeamsInfo(extractTeamId(users)));

        return makeUserDtoList(users, teamMap);
    }

    /**
     * 유저 정보 업데이트
     * @param userId
     * @param updateInfo
     * @return
     */
    public UserDto update(String userId, UpdateUser updateInfo) {
        try {
            UserDto userDto = new UserDto(repository.update(userId, updateInfo));
            TeamDto teamDto = getTeamInfo(userDto.getTeamId());
            userDto.setTeamName(teamDto.getName());

            return userDto;
        } catch (FeignClientException.BadRequest ex) {
            throw new TeamNotExistException("해당 팀은 존재하지 않습니다.", ex);
        }
    }

    /**
     * 유저 삭제 메서드
     * @param userId
     * @return
     */
    public UserDto delete(String userId) {
        return new UserDto(repository.delete(userId));
    }

    /**
     * 팀이 존재하는 지 확인하는 메서드
     * @param teamId
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isExistTeam(Long teamId) {
        log.info("[isExistTeam] circuitBreaker start");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

        // Feign Client 호출 결과를 ResponseEntity<Void>로 받습니다.
        ResponseEntity<Void> responseEntity = circuitBreaker.run(
                () -> teamServiceClient.isExistTeam(teamId),
                throwable -> {
                    log.error("CircuitBreaker fallback triggered. Cause: {}", throwable.getMessage());
                    return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE); // 실패 시 대체 응답 반환
                }
        );

        // 응답의 HTTP 상태 코드가 200번대(성공)인지 확인하여 boolean 값을 반환합니다.
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    /**
     * 팀 정보를 가져오는 메서드
     * @param teamId
     * @return
     */
    @Transactional(readOnly = true)
    public TeamDto getTeamInfo(Long teamId) {
        log.info("[selectTeam] circuitBreaker start");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

        // Feign Client 호출 결과를 ResponseEntity<Void>로 받습니다.
        TeamDto team = circuitBreaker.run(
                () -> teamServiceClient.selectById(teamId).getBody(),
                throwable -> {
                    log.error("CircuitBreaker fallback triggered. Cause: {}", throwable.getMessage());
                    return null;
                }
        );

        return team;
    }

    /**
     * 조건을 통해서 여러팀 정보를 가져오는 메서드
     * @param teamIdList
     * @return
     */
    @Transactional(readOnly = true)
    public List<TeamDto> getTeamsInfo(List<Long> teamIdList) {
        log.info("[selectTeam] circuitBreaker start");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

        TeamCond cond = new TeamCond();
        cond.setTeamIdList(teamIdList);

        // Feign Client 호출 결과를 ResponseEntity<Void>로 받습니다.
        List<TeamDto> TeamDtoList = circuitBreaker.run(
                () -> teamServiceClient.selectByCond(cond).getBody(),
                throwable -> {
                    log.error("CircuitBreaker fallback triggered. Cause: {}", throwable.getMessage());
                    return new ArrayList<>();
                }
        );

        return TeamDtoList;
    }

    private List<Long> extractTeamId(List<User> userList) {
        return userList.stream()
                .distinct()
                .map(User::getTeamId)
                .toList();
    }

    private HashMap<Long, String> teamDtoListToMap(List<TeamDto> teamDtoList) {
        HashMap<Long, String> teamMap = new HashMap<>();

        for (TeamDto teamDto : teamDtoList) {
            if (!teamMap.containsKey(teamDto.getId())) {
                teamMap.put(teamDto.getId(), teamDto.getName());
            }
        }

        return teamMap;
    }

    private List<UserDto> makeUserDtoList(List<User> users, HashMap<Long, String> teamMap) {
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = new UserDto(user);
            userDto.setTeamId(userDto.getTeamId());
            userDto.setTeamName(teamMap.get(userDto.getTeamId()));

            userDtoList.add(userDto);
        }

        return userDtoList;
    }
}

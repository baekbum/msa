package com.example.user_service.controller;

import com.example.user_service.dto.UserDto;
import com.example.user_service.service.UserService;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.UpdateUser;
import com.example.user_service.vo.UserCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> insert(@RequestBody InsertUser insertInfo) {
        log.info("User add process start");
        log.info("InsertInfo : {}", insertInfo);

        UserDto insertDto = userService.insert(insertInfo);
        log.info("insertDto : {}", insertDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(insertDto);
    }

    @GetMapping("/exist/{userId}")
    public ResponseEntity<?> isExist(@PathVariable("userId") String userId) {
        log.info("User exist process start");
        log.info("UserId : {}", userId);

        HttpStatus httpStatus = userService.existById(userId) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).build();
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<?> select(@PathVariable("userId") String userId) {
        log.info("User search process start");
        log.info("UserId : {}", userId);

        UserDto selectDto = userService.selectById(userId);
        log.info("SelectDto : {}", selectDto);

        return ResponseEntity.status(HttpStatus.OK).body(selectDto);
    }

    @PostMapping("/search")
    public ResponseEntity<?> selectByCond(@RequestBody(required = false) UserCond cond) {
        log.info("User search process with cond start");
        log.info("UserCond : {}", cond);

        Page<UserDto> userDtoList = cond.isParamExist() ? userService.selectByCond(cond) : userService.selectAll(cond);
        log.info("UserDtoList size : {}", userDtoList.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> update(@PathVariable("userId") String userId, @RequestBody UpdateUser updateInfo) {
        log.info("User delete process start");
        log.info("UserId : {}", userId);

        UserDto updateDto = userService.update(userId, updateInfo);
        log.info("UpdateDto : {}", updateDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateDto);
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable("userId") String userId) {
        log.info("User delete process start");
        log.info("UserId : {}", userId);

        UserDto deleteDto = userService.delete(userId);
        log.info("DeleteDto : {}", deleteDto);

        return ResponseEntity.status(HttpStatus.OK).body(deleteDto);
    }
}
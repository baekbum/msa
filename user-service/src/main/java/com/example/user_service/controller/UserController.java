package com.example.user_service.controller;

import com.example.user_service.dto.UserDto;
import com.example.user_service.service.UserService;
import com.example.user_service.vo.RequestUser;
import com.example.user_service.vo.ResponseUser;
import com.example.user_service.vo.UserCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> insert(@RequestBody RequestUser user) {
        log.info("User add process start");

        UserDto userDto = new UserDto(user);
        userService.insert(userDto);
        ResponseUser responseUser = new ResponseUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
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

        ResponseUser responseUser = new ResponseUser(userService.selectById(userId));
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PostMapping("/search")
    public ResponseEntity<?> selectByCond(@RequestBody UserCond cond) {
        log.info("User search process start");
        log.info("userCond : {}", cond);

        List<UserDto> userList = userService.selectByCond(cond);
        List<ResponseUser> result = userList.stream()
                .map(ResponseUser::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable("userId") String userId) {
        log.info("User delete process start");
        log.info("UserId : {}", userId);
        return ResponseEntity.ok(new ResponseUser(userService.delete(userId)));
    }


}
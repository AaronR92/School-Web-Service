package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.entity.Group;
import com.aaronr92.schoolwebservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    ResponseEntity<Group> findGroup(@RequestParam(required = false) Integer number,
                                    @RequestParam(required = false) String name) {
        return ResponseEntity.ok(groupService.findGroup(number, name));
    }

    @GetMapping("/all")
    ResponseEntity<List<String>> findAllNames() {
        return ResponseEntity.ok(groupService.findAllNames());
    }

    @PostMapping
    ResponseEntity<Group> addNewGroup(@Valid @RequestBody Group group) {
        URI url = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/group")
                .toUriString());
        return ResponseEntity.created(url).body(groupService.addNewGroup(group));
    }

    @DeleteMapping
    ResponseEntity<Void> deleteGroup(@RequestParam(required = false) Integer number,
                                     @RequestParam(required = false) String name) {
        groupService.deleteGroup(number, name);
        return ResponseEntity.noContent().build();
    }
}

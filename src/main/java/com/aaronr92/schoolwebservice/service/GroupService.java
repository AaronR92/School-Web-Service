package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.entity.Group;
import com.aaronr92.schoolwebservice.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public Group findGroup(Integer number, String name) {
        if (number != null) {
            return groupRepository.findGroupByGroupNumber(number);
        } else if (name != null) {
            return groupRepository.findGroupByGroupName(name);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must provide at least and only one param!");
    }

    public List<String> findAllNames() {
        return groupRepository.findAllNames();
    }

    public Group addNewGroup(Group group) {
        if (groupRepository.exists(Example.of(group))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This group already exists!");
        }
        return groupRepository.save(group);
    }

    public void deleteGroup(Integer number, String name) {
        if (number != null) {
            groupRepository.delete(groupRepository.findGroupByGroupNumber(number));
            return;
        } else if (name != null) {
            groupRepository.delete(groupRepository.findGroupByGroupName(name));
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must provide at least and only one param!");
    }
}

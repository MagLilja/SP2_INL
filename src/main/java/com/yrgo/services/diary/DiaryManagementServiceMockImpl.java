package com.yrgo.services.diary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DiaryManagementServiceMockImpl implements DiaryManagementService {

    private Set<Action> allActions = new HashSet<Action>();

    @Override
    public void recordAction(Action action) {
        allActions.add(action);

    }

    public List<Action> getAllIncompleteActions(String requiredUser) {

        return allActions.stream()
                .filter((a) -> a.getOwningUser().equals(requiredUser) && !a.isComplete())
                .collect(Collectors.toList());
    }

}

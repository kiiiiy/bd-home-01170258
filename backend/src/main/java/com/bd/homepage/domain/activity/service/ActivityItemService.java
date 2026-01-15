package com.bd.homepage.domain.activity.service;

import com.bd.homepage.domain.activity.dto.ActivityItemRequest;
import com.bd.homepage.domain.activity.dto.ActivityItemResponse;
import com.bd.homepage.domain.activity.entity.ActivityCategory;
import com.bd.homepage.domain.activity.entity.ActivityItem;
import com.bd.homepage.domain.activity.repository.ActivityItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityItemService {

    private final ActivityItemRepository activityItemRepository;

    @Transactional(readOnly = true)
    public List<ActivityItemResponse> list(Integer year, ActivityCategory category) {
        return activityItemRepository
                .findByYearAndCategoryOrderByOrderIndexAsc(year, category)
                .stream()
                .map(ActivityItemResponse::from)
                .toList();
    }

    public ActivityItemResponse create(ActivityItemRequest req) {
        ActivityItem saved = activityItemRepository.save(
                ActivityItem.builder()
                        .year(req.getYear())
                        .category(req.getCategory())
                        .title(req.getTitle())
                        .href(req.getHref())
                        .image(req.getImage())
                        .orderIndex(req.getOrderIndex() == null ? 0 : req.getOrderIndex())
                        .isActive(req.getIsActive() == null ? true : req.getIsActive())
                        .build()
        );
        return ActivityItemResponse.from(saved);
    }

    public ActivityItemResponse update(Long id, ActivityItemRequest req) {
        ActivityItem item = activityItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        id + "번 액티비티를 찾을 수 없습니다."
                ));

        item.update(
                req.getYear(),
                req.getCategory(),
                req.getTitle(),
                req.getHref(),
                req.getImage(),
                req.getOrderIndex() == null ? item.getOrderIndex() : req.getOrderIndex(),
                req.getIsActive() == null ? item.getIsActive() : req.getIsActive()
        );

        return ActivityItemResponse.from(item);
    }


    public void delete(Long id) {
        if (!activityItemRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    id + "번 액티비티를 찾을 수 없습니다."
            );
        }
        activityItemRepository.deleteById(id);
    }
}

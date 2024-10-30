package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.MealDTO;
import com.dvk.ct250backend.domain.booking.entity.Meal;
import com.dvk.ct250backend.domain.booking.mapper.MealMapper;
import com.dvk.ct250backend.domain.booking.repository.MealRepository;
import com.dvk.ct250backend.domain.booking.service.MealService;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealServiceImpl implements MealService {

    MealRepository mealRepository;
    FileUtils fileUtils;
    MealMapper mealMapper;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;

    @Override
    @Transactional
    public MealDTO createMeal(MealDTO mealDTO, MultipartFile imgUrl)  throws IOException {
        File convFile = fileUtils.convertMultipartFileToFile(imgUrl);
        String imageUrl = fileUtils.uploadFileToCloudinary(convFile);
        mealDTO.setImgUrl(imageUrl);
        Meal meal = mealMapper.toMeal(mealDTO);
        meal = mealRepository.save(meal);
        return mealMapper.toMealDTO(meal);
    }

    @Override
    @Transactional
    public MealDTO updateMeal(Integer mealId, MealDTO mealDTO, MultipartFile imgUrl) throws IOException, ResourceNotFoundException {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        if(imgUrl != null) {
            String currentImageUrl = meal.getImgUrl();
            if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                String publicId = fileUtils.getPublicIdFromCloudinary(currentImageUrl);
                fileUtils.deleteFileFromCloudinary(publicId);
            }
            File convFile = fileUtils.convertMultipartFileToFile(imgUrl);
            String imageUrl = fileUtils.uploadFileToCloudinary(convFile);
            mealDTO.setImgUrl(imageUrl);
        }
        mealMapper.updateMealFromDTO(meal, mealDTO);
        meal = mealRepository.save(meal);
        return mealMapper.toMealDTO(meal);
    }

    @Override
    public void deleteMeal(Integer mealId) throws ResourceNotFoundException {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        mealRepository.delete(meal);
    }

    @Override
    @Cacheable(value = "meals")
    public List<MealDTO> getAllMeals() {
        return mealRepository.findAll().stream().map(mealMapper::toMealDTO).collect(Collectors.toList());
    }

    @Override
    public Page<MealDTO> getMeals(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Meal> spec = getMealSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Meal> mealPage = mealRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(mealPage.getTotalPages())
                .total(mealPage.getTotalElements())
                .build();
        return Page.<MealDTO>builder()
                .meta(meta)
                .content(mealPage.getContent().stream()
                        .map(mealMapper::toMealDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Meal> getMealSpec(Map<String, String> params) {
        Specification<Meal> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("mealName"))),
                            likePattern
                    )
            );
        }
        return spec;
    }

}

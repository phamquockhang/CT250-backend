package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.mapper.PermissionMapper;
import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.service.PermissionService;
import com.dvk.ct250backend.infrastructure.config.database.PermissionSpecification;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    RequestParamUtils requestParamUtils;

    @Override
    public Page<PermissionDTO> getPermissions(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));

        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params.getOrDefault("sort", ""));
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        Specification<Permission> spec = getPermissionSpec(params);
        org.springframework.data.domain.Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(permissionPage.getTotalPages())
                .total(permissionPage.getTotalElements())
                .build();
        return Page.<PermissionDTO>builder()
                .meta(meta)
                .content(permissionPage.getContent().stream()
                        .map(permissionMapper::toPermissionDTO)
                        .toList())
                .build();
    }

    private Specification<Permission> getPermissionSpec(Map<String, String> params) {
        Specification<Permission> spec = Specification.where(null);
        List<SearchCriteria> methodCriteria = requestParamUtils.getSearchCriteria(params, "method");
        List<SearchCriteria> moduleCriteria = requestParamUtils.getSearchCriteria(params, "module");
        List<List<SearchCriteria>> allCriteria = List.of(methodCriteria, moduleCriteria);
        for (List<SearchCriteria> criteriaList : allCriteria) {
           if(!criteriaList.isEmpty()){
               Specification<Permission> criteriaSpec = Specification.where(null);
               for (SearchCriteria criteria: criteriaList){
                   criteriaSpec = criteriaSpec.or(new PermissionSpecification(criteria));
               }
                spec = spec.and(criteriaSpec);
           }
        }

        return spec;
    }

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionDTO)
                .toList();
    }

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) throws ResourceNotFoundException {
        if (permissionRepository.existsByModuleAndApiPathAndMethod(permissionDTO.getModule(), permissionDTO.getApiPath(), permissionDTO.getMethod())) {
            throw new ResourceNotFoundException("Permission already exists");
        }
        Permission permission = permissionMapper.toPermission(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionDTO(permission);
    }

    @Override
    public void deletePermission(Long id) throws ResourceNotFoundException {
        Permission permission = findPermissionById(id);
        permissionRepository.delete(permission);
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) throws ResourceNotFoundException {
        Permission permissionToUpdate = findPermissionById(id);
        permissionMapper.updatePermissionFromDTO(permissionToUpdate, permissionDTO);
        return permissionMapper.toPermissionDTO(permissionRepository.save(permissionToUpdate));
    }


    private Permission findPermissionById(Long id) throws ResourceNotFoundException {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found permission with id: " + id));
    }


}
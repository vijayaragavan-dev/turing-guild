package com.turingguild.tgms.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationUtil {

    public static <T> ResponseEntity<Map<String, Object>> createPageResponse(Page<T> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        return ResponseEntity.ok(response);
    }
}

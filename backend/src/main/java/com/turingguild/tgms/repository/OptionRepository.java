package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByQuestionIdOrderByOrderIndex(Long questionId);
}

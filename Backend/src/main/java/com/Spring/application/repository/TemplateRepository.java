package com.Spring.application.repository;

import com.Spring.application.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
	Template getTemplatesById(Long id);
}

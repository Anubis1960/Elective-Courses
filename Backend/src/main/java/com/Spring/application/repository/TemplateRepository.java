package com.Spring.application.repository;

import com.Spring.application.entity.Template;
import com.Spring.application.enums.ClassFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
	Template getTemplatesById(Long id);

	@Query("SELECT t FROM Template t WHERE t.classFlag = ?1")
	List<Template> getTemplatesByClassFlag(ClassFlag classFlag);
}

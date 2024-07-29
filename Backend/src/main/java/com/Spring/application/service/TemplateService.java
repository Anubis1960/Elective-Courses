package com.Spring.application.service;

import com.Spring.application.entity.Template;
import com.Spring.application.enums.ClassFlag;
import com.Spring.application.enums.FacultySection;

import java.util.List;
import java.util.Optional;

public interface TemplateService {
	Template getTemplateById(Long id);
	List<Template> getAllTemplates();
	Template addTemplate(String name, Optional<Integer> year, Optional<String> facultySection, String classFlag, int options);
	Template updateTemplate(Long id, String name, Optional<Integer> year, Optional<String> facultySection, int options);
	Template deleteTemplate(Long id);
	List<Template> getByClassFlag(String classFlag);
}

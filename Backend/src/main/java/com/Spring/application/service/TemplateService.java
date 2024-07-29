package com.Spring.application.service;

import com.Spring.application.entity.Template;
import com.Spring.application.enums.ClassFlag;
import com.Spring.application.enums.FacultySection;

import java.util.List;

public interface TemplateService {
	Template getTemplateById(Long id);
	List<Template> getAllTemplates();
	Template addTemplate(String name, Integer year, FacultySection facultySection, int options);
	Template updateTemplate(Long id, String name, Integer year, String facultySection, int options);
	Template deleteTemplate(Long id);
}

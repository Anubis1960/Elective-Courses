package com.Spring.application.service.impl;

import com.Spring.application.entity.Template;
import com.Spring.application.enums.ClassFlag;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.repository.TemplateRepository;
import com.Spring.application.service.TemplateService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public Template getTemplateById(Long id) {
		return templateRepository.findById(id).orElseThrow(EntityNotFoundException::new);
	}

	@Override
	public List<Template> getAllTemplates() {
		return templateRepository.findAll();
	}

	@Override
	public Template addTemplate(String name, Optional<Integer> year, Optional<String> facultySection, String classFlag, int options) {
		// Create object
		Template template = new Template(name, year.orElse(null), facultySection.orElse(null), ClassFlag.valueOf(classFlag), options);
		// Save object
		templateRepository.save(template);
		return template;
	}

	@Override
	public Template updateTemplate(Long id, String name, Optional<Integer> year, Optional<String> facultySection, String classFlag, int options) {
		// Check if template exists
		Template template = templateRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		// Set new values
		template.setName(name);
		template.setYear(year.orElse(null));
		template.setClassFlag(ClassFlag.valueOf(classFlag));
		template.setFacultySection(facultySection.orElse(null));
		template.setOptions(options);

		// Save
		templateRepository.save(template);
		return template;
	}

	@Override
	public Template deleteTemplate(Long id) {
		// Check if template exists
		Template template = templateRepository.findById(id).orElse(null);

		if (template == null) {
			return null;
		}

		templateRepository.delete(template);
		return template;
	}

	@Override
	public List<Template> getByClassFlag(String classFlag) {
		return templateRepository.getTemplatesByClassFlag(ClassFlag.valueOf(classFlag));
	}
}

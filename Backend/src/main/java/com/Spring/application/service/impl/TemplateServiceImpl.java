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
	public Template addTemplate(String name, Integer year, FacultySection facultySection, int options) {
		// Create object
		Template template = new Template(name, year, facultySection, ClassFlag.STUDENT, options);
		// Save object
		templateRepository.save(template);
		return template;
	}

	@Override
	public Template updateTemplate(Long id, String name, Integer year, String facultySection, int options) {
		// Check if template exists
		Template template = templateRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		// Set new values
		template.setName(name);
		template.setYear(year);
		template.setClassFlag(ClassFlag.STUDENT);
		template.setFacultySection(FacultySection.valueOf(facultySection));
		template.setOptions(options);

		// Save
		templateRepository.save(template);
		return template;
	}

	@Override
	public Template deleteTemplate(Long id) {
		// Check if template exists
		Template template = templateRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		templateRepository.delete(template);
		return template;
	}
}

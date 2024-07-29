package com.Spring.application.controller;

import com.Spring.application.dto.TemplateDTO;
import com.Spring.application.entity.Template;
import com.Spring.application.enums.ClassFlag;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/templates")
public class TemplateController {

	@Autowired
	private TemplateService templateService;

	@GetMapping("/")
	public ResponseEntity<List<TemplateDTO>> getTemplates() {
		List<Template> templateList = templateService.getAllTemplates();
		List<TemplateDTO> templateDTOList = TemplateDTO.convertToDTO(templateList);
		return new ResponseEntity<>(templateDTOList, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<TemplateDTO> addTemplate(@RequestParam String name,
												   @RequestParam Optional<Integer> year,
												   @RequestParam Optional<String> facultySection,
												   @RequestParam String classFlag,
												   @RequestParam int options)
	{

		Template template = templateService.addTemplate(name, year, facultySection, classFlag, options);
		TemplateDTO templateDTO = new TemplateDTO(template.getId(),
				template.getName(),
				template.getYear(),
				template.getFacultySection(),
				template.getClassFlag().toString(),
				template.getOptions());
		return new ResponseEntity<>(templateDTO, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TemplateDTO> updateTemplate(@PathVariable("id") Long id,
													  @RequestParam String name,
													  @RequestParam Optional<Integer> year,
													  @RequestParam Optional<String> facultySection,
													  @RequestParam int options)
	{
		templateService.updateTemplate(id, name, year,
				facultySection, options);
		TemplateDTO templateDTO = new TemplateDTO(id, name, year.orElse(null),
				facultySection.orElse(null), null, options);
		return new ResponseEntity<>(templateDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<TemplateDTO> deleteTemplate(@PathVariable("id") Long id) {
		Template template = templateService.deleteTemplate(id);
		if (template == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		TemplateDTO templateDTO = new TemplateDTO(template.getId(), template.getName(),
				template.getYear(), template.getFacultySection(), template.getClassFlag().toString(), template.getOptions());
		return new ResponseEntity<>(templateDTO, HttpStatus.OK);
	}

	@GetMapping("/class-flag")
	public ResponseEntity<List<TemplateDTO>> getByClassFlag(@RequestParam String classFlag) {
		List<Template> templateList = templateService.getByClassFlag(classFlag);
		List<TemplateDTO> templateDTOList = TemplateDTO.convertToDTO(templateList);
		return new ResponseEntity<>(templateDTOList, HttpStatus.OK);
	}
}

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
												   @RequestParam(required = false) Integer year,
												   @RequestParam(required = false) FacultySection facultySection,
												   @RequestParam int options)
	{

		Template template = templateService.addTemplate(name, year, facultySection, options);
		TemplateDTO templateDTO = new TemplateDTO(template.getId(),
				template.getName(),
				template.getYear(),
				template.getFacultySection().toString(),
				ClassFlag.STUDENT.toString(),
				template.getOptions());
		return new ResponseEntity<>(templateDTO, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TemplateDTO> updateTemplate(@PathVariable("id") Long id,
													  @RequestBody TemplateDTO templateDTO)
	{
		templateService.updateTemplate(id, templateDTO.getTemplateName(), templateDTO.getYear(),
				templateDTO.getFacultySection(), templateDTO.getOptions());
		return new ResponseEntity<>(templateDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<TemplateDTO> deleteTemplate(@PathVariable("id") Long id) {
		Template template = templateService.deleteTemplate(id);
		TemplateDTO templateDTO = new TemplateDTO(template.getId(), template.getName(),
				template.getYear(), template.getFacultySection().toString(), template.getClassFlag().toString(), template.getOptions());
		return new ResponseEntity<>(templateDTO, HttpStatus.OK);
	}
}

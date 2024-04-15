package com.Spring.Main.Entity;
import com.Spring.Main.Audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "Students")
public class Student extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "grade", nullable = false)
    private Float grade;

    @Column(name = "faculty_section", nullable = false)
    private String facultySection;

    @Column(name = "year_of_study", nullable = false)
    private Integer year;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(unique = true)
    @MapsId
    private User user;

    public User getUser() {
        return user;
    }

    public Student(Float grade, String facultySection, Integer year) {
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
    }

    public Student(Float grade, String facultySection, Integer year, User user) {
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
        this.user = user;
    }

    public Student() {

    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public User getUsers() {
        return user;
    }

    public void setUsers(User user) {
        this.user = user;
    }
}

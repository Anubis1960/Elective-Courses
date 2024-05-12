
export class Course {
    id: number | undefined;
    name: string | undefined;
    description: string | undefined;
    category: string | undefined;
    maximumStudents: number | undefined;
    facultySection: string | undefined;
    year: number | undefined;
    teacherName: string | undefined;
    numberOfStudents: number | undefined;

    constructor(id: number, name: string, description: string, category: string, maximum_students: number, faculty_section: string, year: number, teacher_name: string, number_of_students: number) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.maximumStudents = maximum_students;
        this.facultySection = faculty_section;
        this.year = year;
        this.teacherName = teacher_name;
        this.numberOfStudents = number_of_students;
    }
}
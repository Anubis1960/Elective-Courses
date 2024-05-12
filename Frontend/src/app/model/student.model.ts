
export class Student {
    id: number | undefined;
    name: string | undefined;
    email: string | undefined;
    role: string | undefined;
    facultySection: string | undefined;
    year: number | undefined;
    grade: number | undefined;

    constructor(id: number, name: string, email: string, role: string, facultySection: string, year: number, grade: number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.facultySection = facultySection;
        this.year = year;
        this.grade = grade;
    }
}
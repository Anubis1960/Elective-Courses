
export class Enrollment {
    id: number | undefined;
    studentId: number | undefined;
    courseId: number | undefined;
    priority: number | undefined;
    status: string | undefined;

    constructor(id: number, student_id: number, course_id: number, priority: number, status: string) {
        this.id = id;
        this.studentId = student_id;
        this.courseId = course_id;
        this.priority = priority;
        this.status = status;
    }
}

export class Enrollment {
    id: number | undefined;
    student_id: number | undefined;
    course_id: number | undefined;
    priority: number | undefined;
    status: string | undefined;

    constructor(id: number, student_id: number, course_id: number, priority: number, status: string) {
        this.id = id;
        this.student_id = student_id;
        this.course_id = course_id;
        this.priority = priority;
        this.status = status;
    }
}
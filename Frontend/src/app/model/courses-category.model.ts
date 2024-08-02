export class CoursesCategory{
    category: string | undefined;
    numberOfCoursesPerCategory: number | undefined;

    constructor(category: string, numberOfCoursesPerCategory:number){
        this.category = category;
        this.numberOfCoursesPerCategory = numberOfCoursesPerCategory;
    }

}
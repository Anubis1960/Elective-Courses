export class Template{
    id : number | undefined;
    name : string | undefined;
    year : number | undefined;
    facultySection : string | undefined;
    classFlag : string | undefined;
    options : number | undefined;

    constructor(id: number, name: string, year: number, facultySection: string, classFlag: string, options: number){
        this.id = id;
        this.name = name;
        this.year = year;
        this.facultySection = facultySection;
        this.classFlag = classFlag;
        this.options = options;
    }
}

export class User {
    id: number | undefined;
    name: string | undefined;
    email: string | undefined;
    role: string | undefined;

    constructor(id: number, name: string, email: string, role: string) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
export class Permission{
    id: number
    name: string

    constructor(id: number, name: string) {
        this.id = id;
        this.name = name;
      }

    equals(other: Permission): boolean {
        return this.name === other.name
    }
}
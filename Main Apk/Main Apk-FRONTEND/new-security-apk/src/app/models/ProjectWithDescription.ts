import { Project } from "./Project";

export class ProjectWithDescription {
    project: Project
    employeeId: number
    description: string

    constructor(project: Project, employeeId: number, description: string) {
        this.project = project;
        this.employeeId = employeeId;
        this.description = description
      }

    equals(other: ProjectWithDescription): boolean {
        return this.project.id === other.project.id && this.employeeId === other.employeeId
    }
}
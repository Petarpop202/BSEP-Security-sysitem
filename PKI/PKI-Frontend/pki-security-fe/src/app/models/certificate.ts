import { Subject } from "./subject";

export interface Certificate {
    subject: Subject
    issuer: Subject
    startDate: Date
    endDate: Date
}
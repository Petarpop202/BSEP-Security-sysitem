import { Subject } from "./subject";

export interface Certificate {
    subject: Subject
    issuer: Subject
    issuerUID: string
    isSelfSigned: boolean
    startDate: Date
    endDate: Date
}
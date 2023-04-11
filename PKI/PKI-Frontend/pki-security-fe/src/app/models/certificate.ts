import { Subject } from "./subject";

export interface Certificate {
    subject: Subject
    issuer: Subject
    issuerUID: string
    isCA: boolean
    isSelfSigned: boolean
    startDate: Date
    endDate: Date
}
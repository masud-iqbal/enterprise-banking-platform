export interface Employee {
    id: number;
    name: string;
    email: string;
    department: string;
    active: boolean;
}
export type CreateEmployeeRequest = Omit<Employee, "id">;
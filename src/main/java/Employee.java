import java.time.LocalDate;

public class Employee {
    private long employeeId;
    private String FirstName;
    private String LastName;
    private LocalDate dateOfBirth;

    public Employee(){

    }

    public Employee(String firstName, String lastName, LocalDate dateOfBirth) {
        FirstName = firstName;
        LastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public Employee(long employeeId, String firstName, String lastName, LocalDate dateOfBirth) {
        this.employeeId = employeeId;
        FirstName = firstName;
        LastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }



    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}

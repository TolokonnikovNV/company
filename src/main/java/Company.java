import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "23693");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Employee getEmployee(long employeeId) {
        try (Connection con = connection) {
            PreparedStatement ps = con.prepareStatement("select * from employee where employee_id = ?");
            ps.setLong(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Employee(rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Employee();
    }

    public Employee NEWgetEmployee(long employeeId) {
        try (Connection con = connection) {
            return getEmployeeInternal(employeeId, con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Employee getEmployeeInternal(long employeeId, Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from employee where employee_id = ?;");
            ps.setLong(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Employee(rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Employee();
    }


    public Job getJob(long jobId) {
        try (Connection con = connection) {
            PreparedStatement ps = con.prepareStatement("select * from job where job_id = ?;");
            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Job(rs.getLong("job_id"),
                        rs.getString("job_title"),
                        rs.getDouble("job_salary_by_one_rate"),
                        rs.getDouble("job_max_rate"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Job();
    }

    private Job getJobInternal(long jobId, Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from job where job_id = ?");
            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Job(rs.getLong("job_id"),
                        rs.getString("job_title"),
                        rs.getDouble("job_salary_by_one_rate"),
                        rs.getDouble("job_max_rate"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Job();
    }


    public List<Employee> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        try (Connection con = connection; Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("select * from employee");
            while (rs.next()) {
                employeeList.add(new Employee(rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    private List<Employee> getEmployeeListInternal(Connection connection) {
        List<Employee> employeeList = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("select * from employee");
            while (rs.next()) {
                employeeList.add(new Employee(rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }


    public List<Job> getJobList() {
        List<Job> jobList = new ArrayList<>();
        try (Connection con = connection; Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("select * from job");
            while (rs.next()) {
                jobList.add(new Job(rs.getLong("job_id"),
                        rs.getString("job_title"),
                        rs.getDouble("job_salary_by_one_rate"),
                        rs.getDouble("job_max_rate")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jobList;
    }

    public List<Job> getJobListInternal(Connection connection) {
        List<Job> jobList = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("select * from job");
            while (rs.next()) {
                jobList.add(new Job(rs.getLong("job_id"),
                        rs.getString("job_title"),
                        rs.getDouble("job_salary_by_one_rate"),
                        rs.getDouble("job_max_rate")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jobList;
    }


    public void assignToJob(long jobId, long employeeId, double rate) {
        try (Connection con = connection) {
            Job tempJob = getJobInternal(jobId, con);
            Employee tempEmployee = getEmployeeInternal(employeeId, con);
            if (rate % 0.25 == 0 &&
                    getEmployeeCurrentRateInternal(con, employeeId) + rate <= 2 &&
                    getJobCurrentRateInternal(con, jobId) + rate <= tempJob.getJobMaxRate()) {
                PreparedStatement ps = con.prepareStatement("select * from job_appointments where job_id = ? and " +
                        "employee_id = ?");
                ps.setLong(1, jobId);
                ps.setLong(2, employeeId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double tempRate = getJobCurrentRateInternal(con, jobId);
                    ps = con.prepareStatement("update job_appointments set rate = ? where employee_id = ? and " +
                            "job_id = ?;");
                    ps.setDouble(1, tempRate + rate);
                    ps.setLong(2, employeeId);
                    ps.setLong(3, jobId);
                    ps.executeUpdate();
                } else {
                    ps = con.prepareStatement("insert into job_appointments values (?,?,?);");
                    ps.setLong(1, jobId);
                    ps.setLong(2, employeeId);
                    ps.setDouble(3, rate);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void firedFromJob(long jobId, long employeeId, double rate) {
        try (Connection con = connection) {
            PreparedStatement ps = connection.prepareStatement("select * from job_appointments where job_id = ? and" +
                    " employee_id = ?;");
            ps.setLong(1, jobId);
            ps.setLong(2, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rate >= rs.getDouble("rate")) {
                    ps = con.prepareStatement("delete from job_appointments where employee_id = ? and job_id = ?;");
                    ps.setLong(1, employeeId);
                    ps.setLong(2, jobId);
                    ps.executeUpdate();

                } else {
                    ps = con.prepareStatement("update job_appointments set rate = ? where employee_id = ? and" +
                            " job_id = ?;");
                    ps.setDouble(1, rs.getDouble("rate") - rate);
                    ps.setLong(2, employeeId);
                    ps.setLong(3, jobId);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Employee> getEmployeeOnThisJob(long jobId) { //делать foreign key для БД
        List<Employee> employeeList = new ArrayList<>();
        try (Connection con = connection) {
            PreparedStatement ps = con.prepareStatement("select * from job_appointments where job_id = ?");
            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                employeeList.add(getEmployeeInternal(rs.getLong("employee_id"), con));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    public List<Job> getEmployeesJobs(long employeeId) {
        List<Job> jobList = new ArrayList<>();
        try (Connection con = connection) {
            PreparedStatement ps = con.prepareStatement("select * from job_appointments where employee_id = ?;");
            ps.setLong(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jobList.add(getJobInternal(rs.getLong("job_id"), con));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jobList;
    }


    public long addEmployee(Employee employee) {
        try (Connection con = connection) {
            for (Employee emp : getEmployeeListInternal(con)) { //сделать запрос с условием
                if (employee.getFirstName().equals(emp.getFirstName()) && employee.getLastName().equals(emp.getLastName())
                        && employee.getDateOfBirth().equals(emp.getDateOfBirth())) {
                    return 0;
                }
            }
            PreparedStatement ps = con.prepareStatement("insert into employee (first_name, last_name, date_of_birth) " +
                    "values (?, ?,?);");
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setDate(3, Date.valueOf(employee.getDateOfBirth()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } // вернуть АйДи нового работника
        return employee.getEmployeeId(); //переделать  на возврат реального ID
    }

    public void deleteEmployee(long employeeId) { // удалить со всех должностей
        try (Connection con = connection) {
            PreparedStatement ps = con.prepareStatement("delete from employee where employee_id = ?;");
            ps.setLong(1, employeeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addJob(Job job) {
        try (Connection con = connection) {
            for (Job job1 : getJobListInternal(con)) {
                if (job.getJobTitle().equals(job1.getJobTitle()) && job.getJobMaxRate() == job1.getJobMaxRate()
                        && job.getJobSalaryByOneRate() == job1.getJobSalaryByOneRate()) {
                    return;
                }
            }
            PreparedStatement ps = con.prepareStatement("insert into job (job_title, job_salary_by_one_rate, " +
                    "job_max_rate) values (?, ?, ?);");
            ps.setString(1, job.getJobTitle());
            ps.setDouble(2, job.getJobSalaryByOneRate());
            ps.setDouble(3, job.getJobMaxRate());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteJob(long jobId) { //уволить всех сотрудников
        try (Connection con = connection) {
            PreparedStatement ps = con.prepareStatement("delete from job where job_id = ?;");
            ps.setLong(1, jobId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private double getJobCurrentRateInternal(Connection connection, long jobId) {
        double rate = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("select * from job_appointments where " +
                    "employee_id = ?;");
            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rate = rate + rs.getDouble("rate");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rate;
    }

    private double getEmployeeCurrentRateInternal(Connection connection, long employeeId) {
        double rate = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("select * from job_appointments where " +
                    "employee_id = ?");
            ps.setLong(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rate = rate + rs.getDouble("rate");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rate;
    }
}

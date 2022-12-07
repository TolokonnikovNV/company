
public class Job {
    private long jobId;
    private String jobTitle;
    private double jobSalaryByOneRate;
    private double jobMaxRate;

    public Job() {
    }

    public Job(long jobId, String jobTitle, double jobSalaryByOneRate, double jobMaxRate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobSalaryByOneRate = jobSalaryByOneRate;
        this.jobMaxRate = jobMaxRate;
    }


    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getJobSalaryByOneRate() {
        return jobSalaryByOneRate;
    }

    public void setJobSalaryByOneRate(double jobSalaryByOneRate) {
        this.jobSalaryByOneRate = jobSalaryByOneRate;
    }

    public double getJobMaxRate() {
        return jobMaxRate;
    }

    public void setJobMaxRate(double jobMaxRate) {
        this.jobMaxRate = jobMaxRate;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobSalaryByOneRate=" + jobSalaryByOneRate +
                ", jobMaxRate=" + jobMaxRate +
                '}';
    }
}

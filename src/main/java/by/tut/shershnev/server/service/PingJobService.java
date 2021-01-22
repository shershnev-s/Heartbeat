package by.tut.shershnev.server.service;

import org.quartz.JobExecutionContext;

public interface PingJobService {

    void execute(JobExecutionContext jobExecutionContext) ;


}

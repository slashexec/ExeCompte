package com.slashexec.facturation.controller.activityreport;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.slashexec.facturation.model.ActivityReport;

@Repository
public interface ActivityReportRepository extends MongoRepository<ActivityReport, String> {

}

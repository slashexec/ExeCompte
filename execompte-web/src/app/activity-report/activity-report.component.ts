import { Component, OnInit } from '@angular/core';
import { AuthenticateService } from '../service/authenticate.service';
import { PeriodDay, ActivityReport, ActivityReportDataService } from '../service/data/activity-report-data.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-activity-report',
  templateUrl: './activity-report.component.html',
  styleUrls: ['./activity-report.component.css']
})
export class ActivityReportComponent implements OnInit {

  id: number;
  username = '';
  activityReport: ActivityReport;
  hideWeekEnds: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticateService: AuthenticateService,
    private activityReportDataService: ActivityReportDataService
  ) { }

  ngOnInit(): void {
    this.username = this.authenticateService.getUserName();
    this.id = this.route.snapshot.params['id'];
    //Init to avoid errors before REST call returns
    this.initActivityReportWithDefault();
    //REST call
    if (this.id != null) {
      this.activityReportDataService.getActivityReportService(this.username, this.id)
        .subscribe(
          data => this.handleActivityReportServiceResponse(data)
        );
    }
  }

  private initActivityReportWithDefault() {
    let today = new Date();
    this.activityReport = new ActivityReport(
      null, //id: number,
      this.username, //username: string,
      new Date(Date.UTC(
        today.getUTCFullYear(),
        today.getUTCMonth(), 1, 0, 0, 0, 0)), //periodStartDate: Date,
      [], //periodDays: PeriodDay[],
      false, //billed: boolean,
      0, //periodTotalTime: number, 
      0, //periodTotalBusinessTime: number, 
      0 ,//periodTotalTeleWorkTime: number,
      '', // prestation
      0, //public unitPrice:number,
      0, //totalHT
      0, //totalTVA
      0 //totalTTC
    );

    this.refreshPerodDays();
  }

  /**
   * refreshPerodDays
   */
  public refreshPerodDays() {
    let currentPeriodDay: Date = this.activityReport.periodStartDate;
    let nextPeriodFirstDay: Date = new Date(this.activityReport.periodStartDate);
    nextPeriodFirstDay.setMonth(nextPeriodFirstDay.getMonth() + 1);

    this.activityReport.periodTotalTime = 0;
    this.activityReport.periodTotalBusinessTime = 0;
    this.activityReport.periodTotalTeleWorkTime = 0;
    this.activityReport.periodDays = [];

    let i: number = 0;
    let periodDayTmp: PeriodDay;
    while (currentPeriodDay < nextPeriodFirstDay) {

      periodDayTmp = this.initPeriodDay(currentPeriodDay);

      if (!this.hideWeekEnds || periodDayTmp.businessDay) {
        this.activityReport.periodDays[i++] = periodDayTmp;
        this.activityReport.periodTotalTime += Number(periodDayTmp.time);
        this.activityReport.periodTotalBusinessTime += (periodDayTmp.businessDay) ? Number(periodDayTmp.time) : 0;
        this.activityReport.periodTotalTeleWorkTime += (periodDayTmp.telework) ? Number(periodDayTmp.time) : 0;
      }

      currentPeriodDay = new Date(Date.UTC(
        currentPeriodDay.getUTCFullYear(),
        currentPeriodDay.getUTCMonth(),
        currentPeriodDay.getUTCDate() + 1, 0, 0, 0, 0));
    }
  }


  private initPeriodDay(day: Date): PeriodDay {
    let dayOfWeek: number = day.getDay();
    //if Week-end ==> isBusinessDay = false;
    //Sunday=0, saturday=6
    let isBusinessDay: boolean = (dayOfWeek != 0 && dayOfWeek != 6);
    let isTelework: boolean = (dayOfWeek == 3); // Wednesday=3
    let time = (isBusinessDay) ? 1 : 0;
    let periodDay = new PeriodDay(day, isBusinessDay, isTelework, time);
    return periodDay;
  }

  /**
   * handleActivityReportServiceResponse
   */
  public handleActivityReportServiceResponse(data) {
    this.activityReport = data;
    //CAUTION: Because it's passed over network (REST call), 
    // activityReport.periodStartDate is a number (timestamp)
    //We are transforming it into a DAte Object
    this.activityReport.periodStartDate = new Date(this.activityReport.periodStartDate);
  }

  /**
   * periodChanged
   */
  public periodChanged($event) {
    let periodparts = $event.split("-");
    let periodYear = Number(periodparts[0]);
    let periodMonth = Number(periodparts[1]) - 1; // month is zero-based
    this.activityReport.periodStartDate.setUTCFullYear(periodYear);
    this.activityReport.periodStartDate.setUTCMonth(periodMonth);
    this.refreshPerodDays();
  }

  /**
   * recomputeTotalDays
   */
  public recomputeTotalDays() {

    this.activityReport.periodTotalTime = 0;
    this.activityReport.periodTotalBusinessTime = 0;
    this.activityReport.periodTotalTeleWorkTime = 0;

    let periodDayTmp: PeriodDay;
    for (let index = 0; index < this.activityReport.periodDays.length; index++) {
      periodDayTmp = this.activityReport.periodDays[index];
      this.activityReport.periodTotalTime += Number(periodDayTmp.time);
      this.activityReport.periodTotalBusinessTime += (periodDayTmp.businessDay) ? Number(periodDayTmp.time) : 0;
      this.activityReport.periodTotalTeleWorkTime += (periodDayTmp.telework) ? Number(periodDayTmp.time) : 0;
    }
  }

  /**
   * validate
   */
  public saveActivityReport() {
    if (this.id == null) {
      //Create new ActivityReport
      this.activityReportDataService.createActivityReport(this.username, this.activityReport)
        .subscribe(
          data => {
            console.log(data);
            this.router.navigate(['list-activity-report']);
          }
        );

    } else {
      //Update ActivityReport
      this.activityReportDataService.updateActivityReport(this.username, this.id, this.activityReport)
        .subscribe(
          data => {
            console.log(data);
            this.router.navigate(['list-activity-report']);
          }
        );
    }
  }

}

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticateService } from '../service/authenticate.service';
import { ActivityReportDataService, ActivityReport } from '../service/data/activity-report-data.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-billing',
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.css']
})
export class BillingComponent implements OnInit {

  id: number;
  unitPrice:number;
  prestation:'';
  username = '';
  activityReport: ActivityReport;
  
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
    if (this.id != -1) {
      this.activityReportDataService.getActivityReportService(this.username, this.id)
        .subscribe(
          data => this.handleActivityReportServiceResponse(data)
        );
    }

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

    this.refreshForm();
  }

  refreshForm() {
    console.log("refresh form");
    this.activityReport.totalHT = this.activityReport.unitPrice * this.activityReport.periodTotalTime;
    this.activityReport.totalTVA = this.activityReport.totalHT * 0.2;
    this.activityReport.totalTTC = this.activityReport.totalHT + this.activityReport.totalTVA;
  }

  /**
   * unitPriceChanged
   */
  public unitPriceChanged($event) {
    console.log($event);
    this.activityReport.unitPrice = Number($event);
    this.refreshForm();
  }

  /**
   * validate
   */
  public billActivityReport() {
    if (this.id == -1) {
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
      this.activityReportDataService.billActivityReport(this.username, this.id, this.activityReport)
        .subscribe(
          data => {
            console.log(data);
            this.router.navigate(['list-activity-report']);
          }
        );
    }
  }

  downloadBillForActivityReport() {
    this.activityReportDataService.billActivityReport(this.username, this.id, this.activityReport)
      .subscribe(
        (response: HttpResponse<Blob>) => {
          console.log(response);
          let filename: string = this.getFileName(response);
          let binaryData = [];
          binaryData.push(response.body);
          let downloadLink = document.createElement('a');
          downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, { type: 'blob' }));
          downloadLink.setAttribute('download', filename);
          document.body.appendChild(downloadLink);
          downloadLink.click();

          //this.refreshActivityReports();
        }
      )
  }

  getFileName(response: HttpResponse<Blob>) {
    let filename: string;
    try {
      let contentDisposition: string = response.headers.get('content-disposition');
      console.log(contentDisposition);
      //attachment; filename=F2020-03_SLASH-EXEC.pdf
      const r = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/
      console.log(r.exec(contentDisposition));
      filename = r.exec(contentDisposition)[1];
    }
    catch (e) {
      filename = 'myfile.pdf'
    }
    return filename;
  }

}

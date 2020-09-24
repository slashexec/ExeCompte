import { Component, OnInit } from '@angular/core';
import { AuthenticateService } from '../service/authenticate.service';
import { PeriodDay, ActivityReport, ActivityReportDataService } from '../service/data/activity-report-data.service';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-list-activity-report',
  templateUrl: './list-activity-report.component.html',
  styleUrls: ['./list-activity-report.component.css']
})
export class ListActivityReportComponent implements OnInit {

  

  username = '';
  period: string = '';
  activityReports: ActivityReport[];
  

  constructor(
    private router: Router,
    private authenticateService: AuthenticateService,
    private activityReportDataService: ActivityReportDataService
    ) { }

  ngOnInit(): void {
    this.username = this.authenticateService.getUserName();
    this.refreshActivityReports();
  }

  /**
   * refreshActivityReports
   */
  public refreshActivityReports() {
    this.activityReportDataService.getAllActivityReportsService(this.username).subscribe(
      response => {
        this.activityReports = response;
      }
    );
  }

  /**
   * editActivityReport
   */
  public editActivityReport(id) {
    this.router.navigate(['list-activity-report', id]);

  }

  /**
   * addActivityReport
   */
  public addActivityReport() {
    console.log (`Add Activity report with id -1`);
    this.router.navigate(['list-activity-report', -1]);

  }

  /**
   * deleteActivityReport
   */
  public deleteActivityReport(id) {
    this.activityReportDataService.deleteActivityReport(this.username, id).subscribe(
      response => {
        this.refreshActivityReports();
      }
    );
  }

  /**
   * deleteActivityReport
   */
  /* public downloadBillForActivityReport(id) {
    this.activityReportDataService.billActivityReport(this.username, id).subscribe(
      response => {
        console.log(response);
        let url = window.URL.createObjectURL(response);
        console.log(url);
        let pwa = window.open(url);
        if (!pwa || pwa.closed || typeof pwa.closed == 'undefined') {
            alert( 'Please disable your Pop-up blocker and try again.');
        }

        //this.refreshActivityReports();
      }
    );
  } */

  /**
   * editActivityReport
   */
  public billActivityReport(id) {
    this.router.navigate(['list-activity-report', id, 'billing']);

  }

  downloadBillForActivityReport(id) {
    this.activityReportDataService.downloadActivityReportBilling(this.username, id)
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

          this.refreshActivityReports();
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

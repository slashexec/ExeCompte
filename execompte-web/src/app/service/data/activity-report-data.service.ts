import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


export class PeriodDay {

  constructor(
    public day: Date,
    public businessDay: boolean,
    public telework: boolean,
    public time: number
    ) {  }
}

export class ActivityReport {
  constructor(
    public id: string,
    public username: string,
    public periodStartDate: Date,
    public periodDays: PeriodDay[],
    public billed: boolean,
    public periodTotalTime: number, 
    public periodTotalBusinessTime: number, 
    public periodTotalTeleWorkTime: number,
    //Billing
    public prestation:string,
    public unitPrice:number, 
    public totalHT:number, 
    public totalTVA:number,
    public totalTTC:number
  ) {  }
}

@Injectable({
  providedIn: 'root'
})
export class ActivityReportDataService {

  constructor(
    private http:HttpClient
  ) { }

  getAllActivityReportsService(username) {
    return this.http.get<ActivityReport[]>(`http://localhost:8080/users/${username}/activityreports`);
  }

  getActivityReportService(username, id) {
    return this.http.get<ActivityReport>(`http://localhost:8080/users/${username}/activityreports/${id}`);
  }
  

  deleteActivityReport(username, id) {
    return this.http.delete (`http://localhost:8080/users/${username}/activityreports/${id}`);
  }

  editActivityReport(username, id) {
    return this.http.get<ActivityReport[]>(`http://localhost:8080/users/${username}/activityreports/${id}`);
  }

  updateActivityReport(username, id, activityReport) {
    return this.http.put(`http://localhost:8080/users/${username}/activityreports/${id}`, activityReport);
  }

  createActivityReport(username, activityReport) {  
    return this.http.post(`http://localhost:8080/users/${username}/activityreports`, activityReport);
  }

  billActivityReport(username, id, activityReport) {  

    return this.http.put(`http://localhost:8080/users/${username}/activityreports/${id}/billing`, activityReport);
  }

  downloadActivityReportBilling(username, id) {   
    return this.http.get<Blob>(`http://localhost:8080/users/${username}/activityreports/${id}/billing/download`, 
    { observe: 'response', responseType: 'blob' as 'json' });
  }
}

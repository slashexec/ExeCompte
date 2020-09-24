import { Pipe, PipeTransform } from '@angular/core';
import { PeriodDay } from '../service/data/activity-report-data.service';


@Pipe({
  name: 'periodDayFilter'
})
export class PeriodDayFilterPipe implements PipeTransform {

  transform(periodDays: PeriodDay[], hideWeekEnds: boolean): PeriodDay[] {

    if (!periodDays || !hideWeekEnds) {
      return periodDays;
    }


    return periodDays.filter(periodDay => {
      let dayOfWeek: number = new Date(periodDay.day).getDay();
      //Sunday=0, saturday=6
      return (dayOfWeek != 0 && dayOfWeek != 6);
    });
  }

}

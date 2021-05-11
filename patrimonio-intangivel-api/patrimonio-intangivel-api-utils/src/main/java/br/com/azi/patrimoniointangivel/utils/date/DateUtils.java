package br.com.azi.patrimoniointangivel.utils.date;

import br.com.azi.patrimoniointangivel.utils.date.exception.DateUtilsException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static Boolean mesCheio(Object startAt, Object finishAt){
        Date start = asDate(startAt);
        Date finish = asDate(finishAt);
        if(!finish.equals(start) && !finish.after(start)){
            throw new DateUtilsException("Data final é anterior a data inicial!");
        }
        Period periodo = Period.between(
            LocalDate.ofInstant(start.toInstant(), ZoneOffset.UTC),
            LocalDate.ofInstant(finish.toInstant(), ZoneOffset.UTC));

        return (periodo.getMonths() != 0 && periodo.getDays() == 0);
    }

    public static int totalDias(Object startAt, Object finishAt){
        Date start = asDate(startAt);
        Date finish = asDate(finishAt);
        if(!finish.equals(start) && !finish.after(start)){
            throw new DateUtilsException("Data final é anterior a data inicial!");
        }
        return (int) ChronoUnit.DAYS.between(
            LocalDateTime.ofInstant(start.toInstant(), ZoneOffset.UTC).minusDays(1),
            LocalDateTime.ofInstant(finish.toInstant(), ZoneOffset.UTC));
    }

    public static int totalMeses(Object startAt, Object finishAt){
        Date start = asDate(startAt);
        Date finish = asDate(finishAt);
        if(!finish.after(start)){
            throw new DateUtilsException("Data final é anterior a data inicial!");
        }
        return (int) ChronoUnit.MONTHS.between(
            LocalDateTime.ofInstant(start.toInstant(), ZoneOffset.UTC),
            LocalDateTime.ofInstant(finish.toInstant(), ZoneOffset.UTC).plusDays(1));
    }

    public static LocalDate somaDias(LocalDate start, int days){
        return start.plusDays(days);
    }

    public static Boolean mesmoMesAno(Object date1, Object date2) {
        LocalDate dateA = asLocalDate(date1);
        LocalDate dateB = asLocalDate(date2);
        return dateA.getMonth().equals(dateB.getMonth()) && dateA.getYear() == dateB.getYear();
    }

    public static Boolean mesAnterior(Object date1, Object date2) {
        LocalDate dateA = asLocalDate(date1);
        LocalDate dateB = asLocalDate(date2);

        if (dateA.getYear() == dateB.getYear()) {
            return  dateA.getMonthValue() < dateB.getMonthValue();
        }else {
            return dateA.getYear() < dateB.getYear();
        }
    }

    public static Date asDate(Object date) {
        if (date instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) date;
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        if (date instanceof LocalDate) {
            LocalDate localDate = (LocalDate) date;
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }else{
            return  (Date) date;
        }
    }

    public static LocalDate asLocalDate(Object date) {
        if (date instanceof LocalDateTime) {
            return ((LocalDateTime) date).toLocalDate();
        }
        if (date instanceof Date) {
            Date dateToConvert = (Date) date;
            return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }else{
            return  (LocalDate) date;
        }
    }

    public static LocalDateTime asLocalDateTime(Object date) {
        if (date instanceof LocalDateTime) {
            return (LocalDateTime) date;
        }
        if (date instanceof Date) {
            Date dateToConvert = (Date) date;
            return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        }else{
            return (LocalDateTime) date;
        }
    }

    public static Boolean mesmoDiaMesAno(Object date1, Object date2){
        LocalDate date1ToConvert = asLocalDate(date1);
        LocalDate date2ToConvert = asLocalDate(date2);
        return date1ToConvert.equals(date2ToConvert);
    }

    public static LocalDateTime converterStringParaLocalDateTime(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateTime = LocalDate.parse(data, formatter);
        return LocalDateTime.of(dateTime, LocalTime.of(0,0));
    }

    public static Date converterStringParaDate(String data){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        LocalDate dateLocal =  LocalDate.parse(data, inputFormatter);
        return Date.from(dateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}





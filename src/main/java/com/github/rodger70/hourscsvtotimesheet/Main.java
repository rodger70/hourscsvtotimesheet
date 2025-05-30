package com.github.rodger70.hourscsvtotimesheet;

import org.antlr.v4.runtime.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Main {

  private static Date startDate;

  public static void main(String[] args) throws Exception {

    for (String arg : args) {
      System.out.println("Processing file: " + arg);

      // create an instance of the lexer
      CSVLexer lexer = new CSVLexer(CharStreams.fromFileName(arg));

      // wrap a token-stream around the lexer
      CommonTokenStream tokens = new CommonTokenStream(lexer);

      // create the parser
      CSVParser parser = new CSVParser(tokens);

      // invoke the entry point of our grammar
      List<List<String>> data = parser.file().data;

      // Create a date/time formatters for the expected formats
      SimpleDateFormat dateSdf = new SimpleDateFormat("EEEE d MMMM yyyy");
      SimpleDateFormat dateSdf2 = new SimpleDateFormat("EEEE d MMMM");
      SimpleDateFormat timeSdf = new SimpleDateFormat("kk:mm");

      // TODO: fix the cross year issue
      // Get the start date from the second row of the CSV
      // Assumes the second row contains a date range like "1 January 2023 to 31 December 2023"
      SimpleDateFormat startDateSdf = new SimpleDateFormat("d MMMM yyyy");
      Date date = startDateSdf.parse(data.get(1).get(0).split(" to ")[0].trim());
      // TODO: use LocalDate for better date handling
      int year = date.getYear() + 1900; // Adjust for Java's year offset

      Date previousDate = date;
      String dateString = "";
      int rowNumber = 1;
      int startOfDayRowNumber = 1;
      int startOfWeekRowNumber = 1;

      // TODO: refactor this to use a visitor or listener
      // display the contents of the CSV source
      for (int r = 2; r < data.size(); r++) {
        List<String> row = data.get(r);

        if (row.size() == 1) {
          // Potentially the date
          try {
            date = dateSdf.parse(row.get(0) + " " + year);
            dateString = row.get(0);
          } catch (Exception e) {
            // Not a date, continue processing
            continue;
          }
        } else if (row.size() == 5) {
          // Potentially a task
          try {
            String taskName = row.get(0);
            Date startTime = timeSdf.parse(row.get(1));
            Date finishTime = timeSdf.parse(row.get(2));

            if (!date.equals(previousDate)) {

              // If the date has changed, print day total
              System.out.print(",=SUM(F" + startOfDayRowNumber + ":F" + (rowNumber - 1) + ")");

              // TODO: handle the case where Sunday is a missing date
              if (previousDate.getDay() == 0) {
                // If the previous date is a Sunday, print week total
                System.out.print(",=SUM(F" + startOfWeekRowNumber + ":F" + (rowNumber - 1) + ")*24");
                startOfWeekRowNumber = rowNumber;
              }

              // and end the row
              System.out.println("");
              
              // Add missing dates
              LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
              LocalDate previousLocalDate = previousDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

              while (ChronoUnit.DAYS.between(previousLocalDate, localDate) > 1) {
                // If the date is more than one day apart
                previousLocalDate = previousLocalDate.plusDays(1);

                System.out.println(dateSdf2.format(Date.from(previousLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())) + ",,,,,=(D" + rowNumber + "-C" + rowNumber + ")-E" + rowNumber + ",=SUM(F" + startOfDayRowNumber + ":F" + (rowNumber - 1) + ")");

                rowNumber++;
              }

              startOfDayRowNumber = rowNumber;
            } else {
              // If the date has not changed, end previous row
              System.out.println("");
            }

            // Print the task row
            System.out.print(dateString + "," + taskName + "," + row.get(1) + "," + row.get(2)
                + ",0:30,=(D" + rowNumber + "-C" + rowNumber + ")-E" + rowNumber);

            previousDate = date;
            rowNumber++;
          } catch (Exception e) {
            // Not a valid task, continue processing
            continue;
          }
        }
      }
    }
  }
}

// End of Main.java

package com.github.rodger70.hourscsvtotimesheet;

import org.antlr.v4.runtime.*;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Main {

  // Date formatters for the expected formats
  // e.g. Tuesday 2 January 2023
  private static final SimpleDateFormat daynameDayMonthYearSdf =
      new SimpleDateFormat("EEEE d MMMM yyyy");
  // e.g. Tuesday 2 January
  private static final SimpleDateFormat daynameDayMonthSdf = new SimpleDateFormat("EEEE d MMMM");
  // e.g. 2 January 2023
  private static final SimpleDateFormat dayMonthYearSdf = new SimpleDateFormat("d MMMM yyyy");
  // e.g. 13:37
  private static final SimpleDateFormat hourMinSdf = new SimpleDateFormat("kk:mm");

  /**
   * Main method to run the application.
   * 
   * @param args Command line arguments, expected to be CSV file paths.
   */
  public static void main(String[] args) throws Exception {
    Main main = new Main(args);

    if (args.length == 0) {
      System.out.println("Usage: java -jar hourscsvtotimesheet.jar [-o outputdir] <csv-file1> <csv-file2> ...");
      return;
    }

    main.run();
  }

	private final String[] args;
	private List<String> files = new ArrayList<String>();

  /**
   * Constructor for the Main class.
   * 
   * @param args Command line arguments, expected to be CSV file paths.
   */
  public Main(String[] args) {
    // Constructor can be used for initialization if needed
    this.args = args;

    handleArgs();    
  }

  private String outputDirectory = "."; // Default output directory is the current directory

  /**
   * Handles command line arguments to set the output directory and collect file paths.
   */
  private void handleArgs() {
    // handle command line arguments
    int i = 0;

    while (args != null && i < args.length) {
			String arg = args[i];
			i++;

      if (arg.startsWith("-o")) { // output directory
        outputDirectory = args[i];
        i++;
      } else { // Assume it's a file path
        files.add(arg);
      }
    }
 }

 /**
   * Runs the main logic of the application.
   * 
   * This method processes each CSV file, parses it, populates tasks, and exports them to CSV format.
   */
  private void run() {
    try {
      for (String file : files) {
        // Parse the CSV file
        List<List<String>> data = parseCSV(file);

        // Populate the tasks from the parsed data
        List<Task> tasks = populateTasks(data);

        // Export the tasks to CSV format
        exportTasksToCSV(tasks);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("An error occurred while processing the CSV files.");
    }
  }

  /**
   * Parses a CSV file and returns the data as a list of lists of strings.
   * 
   * @param csvFilePath The path to the CSV file to parse.
   * @return A list of lists of strings representing the CSV data.
   * @throws Exception If an error occurs while parsing the CSV file.
   */
  private List<List<String>> parseCSV(String csvFilePath) throws Exception {
    System.out.println("Processing file: " + csvFilePath);

    // create an instance of the lexer
    CSVLexer lexer = new CSVLexer(CharStreams.fromFileName(csvFilePath));

    // wrap a token-stream around the lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // create the parser
    CSVParser parser = new CSVParser(tokens);

    // invoke the entry point of our grammar
    return (parser.file().data);
  }

  /**
   * Populates a list of tasks from the parsed CSV data.
   * 
   * @param data The parsed CSV data as a list of lists of strings.
   * @return A list of Task objects populated from the CSV data.
   * @throws Exception If an error occurs while processing the data.
   */
  private List<Task> populateTasks(List<List<String>> data) throws Exception {
    // Get the start date from the second row of the CSV
    // Assumes the second row contains a date range like "1 January 2023 to 31 December 2023"
    Date date = dayMonthYearSdf.parse(data.get(1).get(0).split(" to ")[0].trim());
    // TODO: use LocalDate for better date handling
    int year = date.getYear() + 1900; // Adjust for Java's year offset
    
    // Set the previous date to the start date
    // This will be used to track the last date processed
    Date previousDate = date;

    // Create a list to hold tasks
    List<Task> tasks = new ArrayList<Task>();

    // Iterate through the data starting from the third row
    for (int r = 2; r < data.size(); r++) {
      List<String> row = data.get(r);

      if (row.size() == 1) {
        // Potentially the date or a "[No entries]" row
        try {
          // If the date is in the format "Tuesday 2 January 2023", parse it accordingly
          date = daynameDayMonthYearSdf.parse(row.get(0) + " " + year);

          // how many days are between the previous date and the current date?
          long daysBetween = ChronoUnit.DAYS.between(
              previousDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
              date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());

          // If the date is before the previous date, assume it must be next year
          if (daysBetween < 0) {
            // and increment the year
            year = year + 1;
            date.setYear(year - 1900); // Update the date and Adjust for Java's year offset
          } else {
            // If the date is more than one day apart, add empty rows for the missing dates
            while (daysBetween > 1) {
              // Increment the previous date by one day
              previousDate = Date.from(
                  previousDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                      .plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

              // Add an empty task to the list
              tasks.add(new Task(previousDate));

              // Decrement the daysBetween counter
              daysBetween--;
            }
          }
        } catch (Exception e) {
          // Not a date, could be a "[No entries]" row
          if (row.get(0).equals("[No entries]")) {
            // Add an empty task to the list
            tasks.add(new Task(date));
          }
          // Not a valid date or "[No entries]" row, continue processing
          continue;
        }
      } else if (row.size() == 5) {
        // Potentially a task
        try {
          String taskName = row.get(0);
          Date startTime = hourMinSdf.parse(row.get(1));
          Date finishTime = hourMinSdf.parse(row.get(2));

          // Add the task to the list
          tasks.add(new Task(date, taskName, row.get(1), row.get(2)));
        } catch (Exception e) {
          // Not a valid task, continue processing
          continue;
        }
      }

      // Set the previous date to the current date for the next iteration
      previousDate = date;
    }

    // Return the list of tasks
    return (tasks);
  }

  /**
   * Exports the list of tasks to CSV format.
   * 
   * @param tasks The list of Task objects to export.
   */
  private void exportTasksToCSV(List<Task> tasks) throws Exception {
    // Use PrintStream to write to the CSV file
    PrintStream out = null;

    // Counters to remember the row numbers - these will be reset for each four-week period
    // Don't need to set these, as they will be set when the first task is processed
    int rowNumber = 3; // start of tasks
    int startOfDayRowNumber = rowNumber - 1; // include balance row
    int startOfWeekRowNumber = rowNumber;
    int previousBalanceRowNumber = rowNumber - 1; // Balance row number
    long previousFourWeekPeriod = 0; // Four Week period counter

    for (int i = 0; i < tasks.size(); i++) {
      Task task = tasks.get(i);

      long fourWeekPeriod = ChronoUnit.WEEKS.between(LocalDate.of(1970, 1, 5), // Start week date
                                                                               // (1970-01-05 is a
                                                                               // Monday)
          task.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) / 4;

      if (fourWeekPeriod != previousFourWeekPeriod) {
        // If the four-week period has changed

        // Close the previous output stream if it exists
        if (out != null) {
          out.close();
        }

        // Open a new output stream for the new four-week period
        // out = System.out;
        String filename = outputDirectory + "/"
                + DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .format(task.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                + ".csv";
        System.out.println("Creating file: " + filename);
        out = new PrintStream(filename);

        // Print the header and balance row for the CSV output
        out.println(
            "Date,Task,Start,End,Lunch,Task Total,Day Total,Week Total,Balance,Comments,Conditioned Hours");
        out.println(
            daynameDayMonthYearSdf.format(task.getDate()) + ",Balance brought forward,,,,,,,0,,37");

        // Reset row numbers for the new four-week period
        rowNumber = 3; // start of tasks
        startOfDayRowNumber = rowNumber - 1; // include balance row
        startOfWeekRowNumber = rowNumber;
        previousBalanceRowNumber = rowNumber - 1; // Balance row number

        previousFourWeekPeriod = fourWeekPeriod; // Update the previous four-week period
      }

      // Print date, task name, start time, end time
      out.print(daynameDayMonthYearSdf.format(task.getDate()) + "," + task.getName() + ","
          + task.getStart() + "," + task.getEnd() + ",");

      // Print lunch time
      if (((task.getDate().getDay() == 0) || (task.getDate().getDay() == 6))
          || (task.getStart().isEmpty() && task.getEnd().isEmpty())) {
        // If the task is on a Sunday or Saturday, or there's no start and end time, don't add
        // lunch
        out.print(",");
      } else {
        // otherwise, add a default lunch time of 30 minutes
        out.print("0:30,");
      }

      // Print task total
      if (task.getStart().isEmpty() && task.getEnd().isEmpty()) {
        // If the task has no start or end time, leave total empty
        out.print(",");
      } else {
        // otherwise, calculate the total time spent on the task
        out.print("=(D" + rowNumber + "-C" + rowNumber + ")-E" + rowNumber + ",");
      }

      // Print day total, week total, balance
      if (((i + 1) < tasks.size()) && !(tasks.get(i + 1).getDate().equals(task.getDate()))
          && (!(task.getStart().isEmpty()) || !(task.getEnd().isEmpty()))) {
        // If this is the last task for this date and it has start or end time, calculate
        // day total
        out.print("=SUM(F" + startOfDayRowNumber + ":F" + rowNumber + "),");

        startOfDayRowNumber = rowNumber + 1; // Update start of day row number for next date
      } else {
        // otherwise, just leave it empty
        out.print(",");
      }

      // Print week total and balance
      if (((i + 1) < tasks.size()) && !(tasks.get(i + 1).getDate().equals(task.getDate()))
          && (task.getDate().getDay() == 0)) {
        // If this is the last task for this date and it's a Sunday, calculate week total
        out.print("=SUM(F" + startOfWeekRowNumber + ":F" + rowNumber + ")*24,");

        // and the balance for the next week
        out.print("=I" + previousBalanceRowNumber + "+H" + rowNumber + "-K$2");

        startOfWeekRowNumber = rowNumber + 1; // Update start of week row number for next week
        previousBalanceRowNumber = rowNumber; // Update previous balance row number
      } else {
        // otherwise, just leave both empty
        out.print(",,");
      }

      // Finish the row
      out.println("");
      rowNumber++;
    }

    // Close the output stream
    out.close();
  }
}

// End of Main.java

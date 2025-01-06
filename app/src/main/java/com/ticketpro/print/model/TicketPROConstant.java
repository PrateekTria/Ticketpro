package com.ticketpro.print.model;

/**
 * Created by tspl-7 on 3/9/18.
 */

public class TicketPROConstant {
    public static final String tscData = "\"TEXT 10,30,\\\"3\\\",0,1,1,@1\\n\"\n" +
            "\"TEXT 10,10,\"1\",0,1,1,\"Cite# 123456 Printing from java source\"\n" +
            "\"TEXT 10,10,\"1\",0,1,1,\"Name:ELIE MOUFID SLEIMAN\"\n";
    public static final String ticketData = "Citation: 84-88000262" +
            "Misdemeanor:(N) Traffic:(Y) Nontraffic:(N)" +
            "Date:Mon 09/03/2018 Time:02:29 PM Case#:" +
            "Owner's Responsibility: NO  (VC 40001)" +
            "Name:ELIE MOUFID SLEIMAN" +
            "Addr:10862 MEADS" +
            "City:ORANGE" +
            "State:CA ZIP:92869" +
            "Juvenile(Phone#):" +
            "DL#:V8103677 St:CA   Comm.Lic.:NO " +
            "Birth Date:11/18/1960 Age:57 YRS Juvenile:(NO )" +
            "Sex:M Hair:BLK Eyes:BRN Ht:5'0\" Wt:165 Race:F" +
            "Lic/VIN:DUUJFHF GSZG" +
            "State:CA Reg Exp:JUL/23 Yr.of Veh: 2011" +
            "Make:AUDI Model:A3" +
            "Body Style:CSUV Color:BROWN" +
            "_________________________________________________________" +
            "COMMERCIAL VEH.(VC 15210(b)): NO " +
            "HAZ. MAT.(VC 353): NO " +
            "Evid. of Financial Resp: NA" +
            "Reg.Owner:HDHX" +
            "Address:10862 MEADS" +
            "City:ORANGE St:CA ZIP:92869" +
            "Correctable Violation           Booking Required:NO " +
            "(VC 40810)                        (see reverse)" +
            "(Yes/No) Code Section/Description      Misd./Infrac." +
            "NO 	1061(A)" +
            "I" +
            "AIR GOVERNOR ADJUSTMENT CUT IN PRESSURE" +
            "Speed Approx:21  PF/Max Spd:25  Veh.Lmt: 45" +
            "Safe: 30   @45 " +
            "Location of Violation(s) at: SAN MATEO" +
            "1ST AVE" +
            "Conditions:                Accident: YES" +
            "Light:DAWN	Traffic:HEAVY" +
            "Road:GRAVEL	Weather:RAIN" +
            "WITHOUT ADMITTING GUILT, I PROMISE TO APPEAR" +
            "AT THE TIME AND PLACE INDICATED BELOW." +
            "When: 11/01/2018 8:30 AM" +
            "What to do: SEE INSTRUCTIONS ON THE REVERSE" +
            "Where: BEFORE A JUDGE OR CLERK OF THE" +
            "TRAFFIC-SUPERIOR COURT" +
            "500 COUNTY CENTER/REDWOOD CITY" +
            "(650) 363-4000" +
            "To be notified: (No)" +
            "[NO ]  Violations not committed in my presence," +
            "declared on information or belief." +
            "I declare under penalty of perjury under the laws of" +
            "the State of California the foregoing is true" +
            "and correct." +
            "Executed at SAN MATEO" +
            "TURBO -9999							09/03/2018" +
            "_________________________________  ________________" +
            "Arresting or Citing Officer        Declaration Date" +
            " 							" +
            "_________________________________  ________________" +
            "Arresting Officer if different     Declaration Date" +
            "from the Citing Officer" +
            "_________________________________________________________" +
            "Judicial Council of California Form New 06-25-15" +
            "(VC 40500(b),40513(b), 40522, and 40600; PC 853.9" +
            "and 95931) TR-145     DEFENDANT COPY";

    public static String ticketDataToBePrinted = "Citation: 84-88000262\n" +
            "Misdemeanor:(N) Traffic:(Y) Nontraffic:(N)\n" +
            "Date:Mon 09/03/2018 Time:02:29 PM Case#:\n" +
            "Owner's Responsibility: NO  (VC 40001)\n" +
            "\n" +
            "Name:ELIE MOUFID SLEIMAN\n" +
            "Addr:10862 MEADS\n" +
            "City:ORANGE\n" +
            "State:CA ZIP:92869\n" +
            "Juvenile(Phone#):\n" +
            "\n" +
            "DL#:V8103677 St:CA   Comm.Lic.:NO \n" +
            "Birth Date:11/18/1960 Age:57 YRS Juvenile:(NO )\n" +
            "Sex:M Hair:BLK Eyes:BRN Ht:5'0\" Wt:165 Race:F\n" +
            "\n" +
            "Lic/VIN:DUUJFHF GSZG\n" +
            "State:CA Reg Exp:JUL/23 Yr.of Veh: 2011\n" +
            "Make:AUDI Model:A3\n" +
            "Body Style:CSUV Color:BROWN\n" +
            "_________________________________________________________\n" +
            "COMMERCIAL VEH.(VC 15210(b)): NO \n" +
            "HAZ. MAT.(VC 353): NO \n" +
            "Evid. of Financial Resp: NA\n" +
            "Reg.Owner:HDHX\n" +
            "Address:10862 MEADS\n" +
            "City:ORANGE St:CA ZIP:92869\n" +
            "Correctable Violation           Booking Required:NO \n" +
            "(VC 40810)                        (see reverse)\n" +
            "(Yes/No) Code Section/Description      Misd./Infrac.\n" +
            "\n" +
            "NO \t1061(A)\n" +
            "\n" +
            "I\n" +
            "AIR GOVERNOR ADJUSTMENT CUT IN PRESSURE\n" +
            "\n" +
            "Speed Approx:21  PF/Max Spd:25  Veh.Lmt: 45\n" +
            "Safe: 30   @45 \n" +
            "Location of Violation(s) at: SAN MATEO\n" +
            "1ST AVE\n" +
            "Conditions:                Accident: YES\n" +
            "Light:DAWN\tTraffic:HEAVY\n" +
            "Road:GRAVEL\tWeather:RAIN\n" +
            "WITHOUT ADMITTING GUILT, I PROMISE TO APPEAR\n" +
            "AT THE TIME AND PLACE INDICATED BELOW.\n" +
            "When: 11/01/2018 8:30 AM\n" +
            "What to do: SEE INSTRUCTIONS ON THE REVERSE\n" +
            "Where: BEFORE A JUDGE OR CLERK OF THE\n" +
            "TRAFFIC-SUPERIOR COURT\n" +
            "500 COUNTY CENTER/REDWOOD CITY\n" +
            "(650) 363-4000\n" +
            "To be notified: (No)\n" +
            "[NO ]  Violations not committed in my presence,\n" +
            "declared on information or belief.\n" +
            "\n" +
            "I declare under penalty of perjury under the laws of\n" +
            "the State of California the foregoing is true\n" +
            "and correct.\n" +
            "\n" +
            "Executed at SAN MATEO\n" +
            "\n" +
            "TURBO -9999\t\t\t\t\t\t\t09/03/2018\n" +
            "_________________________________  ________________\n" +
            "Arresting or Citing Officer        Declaration Date\n" +
            "\n" +
            " \t\t\t\t\t\t\t\n" +
            "_________________________________  ________________\n" +
            "Arresting Officer if different     Declaration Date\n" +
            "from the Citing Officer\n" +
            "_________________________________________________________\n" +
            "Judicial Council of California Form New 06-25-15\n" +
            "(VC 40500(b),40513(b), 40522, and 40600; PC 853.9\n" +
            "and 95931) TR-145     DEFENDANT COPY";
}

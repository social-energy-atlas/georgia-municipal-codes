//next-remove all &'s in the file (use bartow county as example) and also do charter and related laws or special acts automatically
import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.lang.Object;
public class MuniMarkup {
    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("Input the file name: ");
        String fileName = input.next();
        File file = new File("ga-muni-" + fileName + "-code-full" + ".txt");
        File out = new File("ga-muni-" + fileName + "-code-full" + "-markup.xml");
        FileOutputStream outText = new FileOutputStream(out); 
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outText));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "\n<?xml-model href=\"http://www.tei-c.org/release/xml/tei/c"
            + "ustom/schema/relaxng/tei_all.rng\" type=\"application/xml\" "
            + "schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n<?xml-"
            + "model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/"
            + "relaxng/tei_all.rng\" type=\"application/xml\"\n   schematypens="
            + "\"http://purl.oclc.org/dsdl/schematron\"?>\n<TEI xmlns=\"http://"
            + "www.tei-c.org/ns/1.0\">\n   <teiHeader>\n   <fileDesc>\n       <titleStmt>"
            + "\n         <title><!-- " + fileName + " --> Code of Ordinances</title>"
            + "\n         <editor xml:id=\"JHT\">Jacqueline Hettel Tidwell</editor>"
            + "\n         <editor xml:id=\"E\">Emily Grubert</editor>\n         <respStmt "
            + "xml:id=\"AG\">\n            <resp>Text markup</resp>"
            + "\n            <name>Anna Gardner</name>\n         </respStmt>"
            + "\n      </titleStmt>\n      <publicationStmt>\n         <distributor>SolarSMART"
            + " - Social Responsibility CODEC Lab</distributor>\n         <address>"
            + "\n            <addrLine>University of Georgia</addrLine>\n            <addrLine>Park Hall</addrLine>"
            + "\n            <addrLine>Athens, GA 30602</addrLine>\n         </address>"
            + "\n      </publicationStmt>\n      <notesStmt><note><!-- Jacque to determine if we need or not. --></note></notesStmt>"
            + "\n      <sourceDesc>\n         <p>Original code accessed at <!-- https://library.municode.com/ga/" + fileName + "/codes/code_of_ordinances -->. Adopted <!-- "
            + "January 17 2002 -->. Effective <!-- January 17 2002 -->.</p>\n      </sourceDesc>\n   </fileDesc>"
            + "\n   <encodingDesc>\n      <projectDesc><p><!-- Jacque to update with encoding description text. --></p></projectDesc>"
            + "\n   </encodingDesc>\n   <profileDesc>\n      <langUsage><language ident=\"en\">English</language></langUsage>"
            + "\n      <textDesc n=\"code-of-ordinance\">\n         <channel>print</channel>\n         <constitution type=\"single\"/>"
            + "\n         <derivation type=\"original\"/>\n         <domain type=\"policy\"/>\n         <factuality type=\"fact\"/>"
            + "\n         <interaction type=\"none\"/>\n         <preparedness type=\"formulaic\"/>\n         <purpose type=\"inform\" degree=\"high\"/>"
            + "\n      </textDesc>\n   </profileDesc>\n   <revisionDesc>\n      <change when=\"2019-09-10\" who=\"#JHT\">Created header structure for template.</change>"
            + "\n      <change when=\"2019-10-14\" who=\"#AMG\">Added Municipal code and structured it.</change>"
            + "\n   </revisionDesc>\n   </teiHeader>\n  <text>\n     <body>\n        <div type=\"cover-info\">\n           <p>");
        Scanner prelimScan = new Scanner(file);
        String[] prelimStringCheck;
        String prelimNextLine;
        boolean hasPreface = false;
        boolean hasCharter = false;
        boolean hasRelatedLaws = false;
        boolean hasChapters = false;
        while (prelimScan.hasNextLine()) {
            prelimNextLine = prelimScan.nextLine();
            String trimmedPrelimNextLine = prelimNextLine.trim();
            if(!trimmedPrelimNextLine.isEmpty()) {
                prelimStringCheck = prelimNextLine.split(" ");
                if (prelimStringCheck[0].equals("PREFACE")) {
                    hasPreface = true;
                    //System.out.println("has preface.");
                }
                if (prelimStringCheck[0].equals("CHARTER[1]")) {
                    hasCharter = true;
                }
                if (prelimStringCheck.length >= 4) {
                    if (prelimStringCheck[0].equals("PART") && prelimStringCheck[1].equals("I") && prelimStringCheck[2].equals("-")
                        && (prelimStringCheck[3].equals("CHARTER[1]") || prelimStringCheck[3].equals("CHARTER") || prelimStringCheck[3].equals("CITY"))) {
                        //charter comes like CHARTER[1] and like PART 1 - CHARTER (or CHARTER[1] or AND RELATED LAWS) or like PART 1 - CITY CHARTER
                        hasCharter = true;
                    }
                    if (prelimStringCheck.length >= 5) {
                        if (prelimStringCheck[0].equals("Subpart") && prelimStringCheck[1].equals("B")
                            && prelimStringCheck[2].equals("-") && prelimStringCheck[3].equals("RELATED")
                            && prelimStringCheck[4].equals("LAWS")) {
                            hasRelatedLaws = true;
                        //System.out.println("Acworth has related laws!!");
                        }
                    }
                }
                if (prelimStringCheck.length >= 2) {
                    if (prelimStringCheck[0].equals("Chapter") && isNumeric(prelimStringCheck[1])) {
                        hasChapters = true;
                        //System.out.println("has chapter.");
                    }
                }
            }
        }


        Scanner scan = new Scanner(file);
        String[] stringCheck;
        String nextLine;
       //if (hasPreface && !hasCharter && !hasRelatedLaws && hasChapters) {
        if (hasPreface && !hasCharter && !hasRelatedLaws && hasChapters) {
            //System.out.println("This document has a preface and chapters:\n");
            int chapterCounter = 0;
            boolean prefaceStarted = false;
            while(scan.hasNextLine() && !prefaceStarted) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim(); 
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck[0].equals("PREFACE")) {
                        bw.write(nextLine + "\n");
                        bw.write("</p>\n        </div>\n         <div type=\"preface\">\n            <p>" + nextLine);
                        prefaceStarted = true;
                    } else {
                        bw.write(nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            while(scan.hasNextLine()) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim();
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck.length >= 3) {
                        if(stringCheck[0].equals("Chapter") && isNumeric(stringCheck[1]) && stringCheck[2].equals("-")) {
                            chapterCounter ++;
                            if (chapterCounter == 1) {
                                //System.out.println("Chapter 1 found");
                                bw.write("</p>\n</div>\n         <div type=\"charter\"></div>\n         <div type=\"related-laws\"></div>\n         <div type=\"special-acts\"></div>\n         <div type=\"code\">\n");
                                bw.write("            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            } else {
                                bw.write("               </p>\n            </div>\n            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            }
                        } else {
                            bw.write("                  " + nextLine + "\n");
                        }
                    } else {
                        bw.write("                  " + nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            bw.write("</p>\n</div>\n         </div>\n     </body>\n</text>\n</TEI>");
            //System.out.println("Chapters complete");
            bw.close();   

        }
        //just has chapters
        if (!hasPreface && !hasRelatedLaws && !hasCharter && hasChapters) {
            //System.out.println("This document has a preface and chapters:\n");
            int chapterCounter = 0;
            while(scan.hasNextLine()) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim();
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck.length >= 3) {
                        if(stringCheck[0].equals("Chapter") && isNumeric(stringCheck[1]) && stringCheck[2].equals("-")) {
                            chapterCounter ++;
                            if (chapterCounter == 1) {
                                //System.out.println("Chapter 1 found");
                                bw.write("</p>\n        </div>\n         <div type=\"preface\"></div>");
                                bw.write("\n         <div type=\"charter\"></div>\n         <div type=\"related-laws\"></div>\n         <div type=\"special-acts\"></div>\n         <div type=\"code\">\n");
                                bw.write("            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            } else {
                                bw.write("               </p>\n            </div>\n            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            }
                        } else {
                            bw.write("                  " + nextLine + "\n");
                        }
                    } else {
                        bw.write("                  " + nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            bw.write("</p>\n</div>\n         </div>\n     </body>\n</text>\n</TEI>");
            //System.out.println("Chapters complete");
            bw.close();   

        }
        if (hasPreface && hasCharter && !hasRelatedLaws && hasChapters) {
            //System.out.println("This document has a preface and chapters:\n");
            int chapterCounter = 0;
            boolean prefaceStarted = false;
            boolean charterStarted = false;
            while(scan.hasNextLine() && !prefaceStarted) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim(); 
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck[0].equals("PREFACE")) {
                        bw.write(nextLine + "\n");
                        bw.write("</p>\n        </div>\n         <div type=\"preface\">\n            <p>" + nextLine);
                        prefaceStarted = true;
                    } else {
                        bw.write(nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            while(scan.hasNextLine() && prefaceStarted && !charterStarted) {
                //charter comes like CHARTER[1] and like PART 1 - CHARTER (or CHARTER[1] or AND RELATED LAWS) or like PART 1 - CITY CHARTER
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim(); 
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck[0].equals("CHARTER[1]")) {
                        bw.write(nextLine + "\n");
                        bw.write("</p>\n        </div>\n         <div type=\"charter\">\n            <p>" + nextLine);
                        charterStarted = true;
                    } else if (stringCheck.length >= 4) {
                        if (stringCheck[0].equals("PART") && stringCheck[1].equals("I") && stringCheck[2].equals("-") && (stringCheck[3].equals("CHARTER") || stringCheck[3].equals("CITY") || stringCheck[3].equals("CHARTER[1]"))) {
                            bw.write(nextLine + "\n");
                            bw.write("</p>\n        </div>\n         <div type=\"charter\">\n            <p>" + nextLine);
                            charterStarted = true;    
                        } else {
                            bw.write(nextLine + "\n");
                        }
                    } else {
                        bw.write(nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            while(scan.hasNextLine()) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim();
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck.length >= 3) {
                        if(stringCheck[0].equals("Chapter") && isNumeric(stringCheck[1]) && stringCheck[2].equals("-")) {
                            chapterCounter ++;
                            if (chapterCounter == 1) {
                                //System.out.println("Chapter 1 found");
                                bw.write("</p>\n         </div>\n         <div type=\"related-laws\"></div>\n         <div type=\"special-acts\"></div>\n         <div type=\"code\">\n");
                                bw.write("            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            } else {
                                bw.write("               </p>\n            </div>\n            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            }
                        } else {
                            bw.write("                  " + nextLine + "\n");
                        }
                    } else {
                        bw.write("                  " + nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            bw.write("</p>\n</div>\n         </div>\n     </body>\n</text>\n</TEI>");
            //System.out.println("Chapters complete");
            bw.close();   

        }
        if (hasPreface && hasCharter && hasRelatedLaws && hasChapters) {
            //System.out.println("This document has a preface and chapters:\n");
            int chapterCounter = 0;
            boolean prefaceStarted = false;
            boolean charterStarted = false;
            boolean relatedLawsStarted = false;
            while(scan.hasNextLine() && !prefaceStarted && !charterStarted && !relatedLawsStarted) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim(); 
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck[0].equals("PREFACE")) {
                        bw.write(nextLine + "\n");
                        bw.write("</p>\n        </div>\n         <div type=\"preface\">\n            <p>" + nextLine);
                        prefaceStarted = true;
                    } else {
                        bw.write(nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            while(scan.hasNextLine() && prefaceStarted && !charterStarted && !relatedLawsStarted) {
                //charter comes like CHARTER[1] and like PART 1 - CHARTER (or CHARTER[1] or AND RELATED LAWS) or like PART 1 - CITY CHARTER
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim(); 
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck[0].equals("CHARTER[1]")) {
                        bw.write(nextLine + "\n");
                        bw.write("</p>\n        </div>\n         <div type=\"charter\">\n            <p>" + nextLine);
                        charterStarted = true;
                    } else if (stringCheck.length >= 4) {
                        if (stringCheck[0].equals("PART") && stringCheck[1].equals("I") && stringCheck[2].equals("-") && (stringCheck[3].equals("CHARTER") || stringCheck[3].equals("CITY") || stringCheck[3].equals("CHARTER[1]"))) {
                            bw.write(nextLine + "\n");
                            bw.write("</p>\n        </div>\n         <div type=\"charter\">\n            <p>" + nextLine);
                            charterStarted = true;    
                        } else {
                            bw.write(nextLine + "\n");
                        }
                    } else {
                        bw.write(nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            while(scan.hasNextLine() && prefaceStarted && charterStarted && !relatedLawsStarted) {
                //charter comes like CHARTER[1] and like PART 1 - CHARTER (or CHARTER[1] or AND RELATED LAWS) or like PART 1 - CITY CHARTER
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim(); 
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck.length >= 5) {
                        if (stringCheck[0].equals("Subpart") && stringCheck[1].equals("B") && stringCheck[2].equals("-") && stringCheck[3].equals("RELATED") && stringCheck[4].equals("LAWS")) {
                            bw.write(nextLine + "\n");
                            bw.write("</p>\n        </div>\n         <div type=\"related-laws\">\n            <p>" + nextLine);
                            relatedLawsStarted = true;    
                        } else {
                            bw.write(nextLine + "\n");
                        }
                    } else {
                        bw.write(nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }            
            while(scan.hasNextLine()) {
                nextLine = scan.nextLine();
                nextLine = nextLine.replace("&", "and");
                String trim = nextLine.trim();
                if(!trim.isEmpty()) {
                    stringCheck = nextLine.split(" ");
                    if (stringCheck.length >= 3) {
                        if(stringCheck[0].equals("Chapter") && isNumeric(stringCheck[1]) && stringCheck[2].equals("-")) {
                            chapterCounter ++;
                            if (chapterCounter == 1) {
                                //System.out.println("Chapter 1 found");
                                bw.write("</p>\n         </div>\n         <div type=\"special-acts\"></div>\n         <div type=\"code\">\n");
                                bw.write("            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            } else {
                                bw.write("               </p>\n            </div>\n            <div type=\"chapter\" n=\"" + stringCheck[1] + "\">\n               <head>" + nextLine + "</head>\n               <p>");
                            }
                        } else {
                            bw.write("                  " + nextLine + "\n");
                        }
                    } else {
                        bw.write("                  " + nextLine + "\n");
                    }
                } else {
                    bw.write("\n");
                }
            }
            bw.write("</p>\n</div>\n         </div>\n     </body>\n</text>\n</TEI>");
            //System.out.println("Chapters complete");
            bw.close();   

        }
    }
}
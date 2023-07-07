package jdiff;

import java.io.*;
import java.util.*;
import com.sun.javadoc.*;
import javax.tools.Diagnostic;
import jdk.javadoc.doclet.Doclet.Option;

/** 
 * Class to handle options for JDiff.
 *
 * See the file LICENSE.txt for copyright details.
 * @author Matthew Doar, mdoar@pobox.com
 */
public class Options {

    public static String authorid = "";
    public static String versionid = "";
    public static boolean classlist = false;
    public static String title = "";
    public static boolean docletid = false;
    public static String evident = "";
    public static List<String> skippkg = new ArrayList();
    public static List<String> skipclass = new ArrayList();
    public static int execdepth = 0;

    /**
     * The name of the file where the XML representing the old API is
     * stored.
     */
    static String oldFileName = "old_java.xml";

    /**
     * The name of the directory where the XML representing the old API is
     * stored.
     */
    static String oldDirectory = null;

    /**
     * The name of the file where the XML representing the new API is
     * stored.
     */
    static String newFileName = "new_java.xml";

    /**
     * The name of the directory where the XML representing the new API is
     * stored.
     */
    static String newDirectory = null;

    /** If set, then generate the XML for an API and exit. */
    static boolean writeXML = false;

    /** If set, then read in two XML files and compare their APIs. */
    static boolean compareAPIs = false;

    public static Set<? extends Option> getSupportedOptions() {
        Set<Option> options = new HashSet<>();

        // Standard options

        options.add(
                new Option() {
                    private final List<String> names = List.of("-authorid");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<author>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.authorid = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-versionid");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<version>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.versionid = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-d");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Destination directory for output HTML files";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<directory>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.outputDir = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-classlist");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.classlist = true;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-title");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<title>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.title = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-docletid");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.docletid = true;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-evident");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<evident>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.evident = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-skippkg");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<package>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.skippkg.add(arguments.get(0));
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-skipclass");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<class>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.skipclass.add(arguments.get(0));
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-execdepth");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<depth>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.execdepth = Integer.parseInt(arguments.get(0));
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-version");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        System.out.println("JDiff version: " + JDiff.version);
                        System.exit(0);
                        return true;
                    }
                }
        );

        // Options to control JDiff

        options.add(
                new Option() {
                    private final List<String> names = List.of("-apiname");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<Name of a version>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.skipclass.add(arguments.get(0));
                        String filename = arguments.get(0);
                        RootDocToXML.apiIdentifier = filename;
                        filename = filename.replace(' ', '_');
                        RootDocToXML.outputFileName =  filename + ".xml";
                        Options.writeXML = true;
                        Options.compareAPIs = false;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-oldapi");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<Name of a version>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        String filename = arguments.get(0);
                        filename = filename.replace(' ', '_');
                        Options.oldFileName =  filename + ".xml";
                        Options.writeXML = false;
                        Options.compareAPIs = true;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-newapi");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<Name of a version>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        String filename = arguments.get(0);
                        filename = filename.replace(' ', '_');
                        Options.newFileName =  filename + ".xml";
                        Options.writeXML = false;
                        Options.compareAPIs = true;
                        return true;
                    }
                }
        );

        // Options to control the location of the XML files

        options.add(
                new Option() {
                    private final List<String> names = List.of("-apidir");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<directory>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        RootDocToXML.outputDirectory = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-oldapidir");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Location of the XML file for the old API";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<directory>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.oldDirectory = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-newapidir");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Location of the XML file for the new API";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<directory>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Options.newDirectory = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-usercommentsdir");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Path to dir containing the user_comments* file(s)";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<directory>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.commentsDir = arguments.get(0);
                        return true;
                    }
                }
        );

        // Options for the exclusion level for classes and members

        options.add(
                new Option() {
                    private final List<String> names = List.of("-excludeclass");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Exclude classes which are not public, protected etc";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() {
                        return "[public|protected|package|private]";
                    }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        String level = arguments.get(0);
                        if (level.compareTo("public") != 0 &&
                            level.compareTo("protected") != 0 &&
                            level.compareTo("package") != 0 &&
                            level.compareTo("private") != 0) {
                            JDiff.reporter.print(Diagnostic.Kind.ERROR,
                                    "Level specified after -excludeclass option must be one of (public|protected|package|private).");
                            return false;
                        }
                        RootDocToXML.classVisibilityLevel = level;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-excludemember");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Exclude members which are not public, protected etc";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() {
                        return "[public|protected|package|private]";
                    }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        String level = arguments.get(0);
                        if (level.compareTo("public") != 0 &&
                            level.compareTo("protected") != 0 &&
                            level.compareTo("package") != 0 &&
                            level.compareTo("private") != 0) {
                            JDiff.reporter.print(Diagnostic.Kind.ERROR,
                                    "Level specified after -excludemember option must be one of (public|protected|package|private).");
                            return false;
                        }
                        RootDocToXML.memberVisibilityLevel = level;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-firstsentence");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() {
                        return "Save only the first sentence of each comment block with the API.";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        RootDocToXML.saveAllDocs = false;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-docchanges");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() {
                        return "Report changes in Javadoc comments between the APIs";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.reportDocChanges = true;
                        Diff.noDocDiffs = false;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-packagesonly");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        RootDocToXML.packagesOnly = true;
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-showallchanges");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        Diff.showAllChanges = true;
                        return true;
                    }
                }
        );

        // Option to change the location for the existing Javadoc
        // documentation for the new API. Default is "../"

        options.add(
                new Option() {
                    private final List<String> names = List.of("-javadocnew");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() {
                        return "<location of existing Javadoc files for the new API>";
                    }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.newDocPrefix = arguments.get(0);
                        return true;
                    }
                }
        );

        // Option to change the location for the existing Javadoc
        // documentation for the old API. Default is null.

        options.add(
                new Option() {
                    private final List<String> names = List.of("-javadocold");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() {
                        return "<location of existing Javadoc files for the old API>";
                    }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.oldDocPrefix = arguments.get(0);
                        return true;
                    }
                }
        );

        options.add(
                new Option() {
                    private final List<String> names = List.of("-baseuri");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Use \"base\" as the base location of the various DTDs and Schemas used by JDiff";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<base>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        RootDocToXML.baseURI = arguments.get(0);
                        return true;
                    }
                }
        );

        // Option not to suggest comments at all

        options.add(
                new Option() {
                    private final List<String> names = List.of("-nosuggest");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Do not add suggested comments to all, or the removed, added or changed sections";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() {
                        return "[all|remove|add|change]";
                    }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        String level = arguments.get(0);
                        if (level.compareTo("all") != 0 &&
                            level.compareTo("remove") != 0 &&
                            level.compareTo("add") != 0 &&
                            level.compareTo("change") != 0) {
                            JDiff.reporter.print(Diagnostic.Kind.ERROR,
                                    "Level specified after -nosuggest option must be one of (all|remove|add|change).");
                            return false;
                        }
                        if (level.compareTo("removal") == 0)
                            HTMLReportGenerator.noCommentsOnRemovals = true;
                        else if (level.compareTo("add") == 0)
                            HTMLReportGenerator.noCommentsOnAdditions = true;
                        else if (level.compareTo("change") == 0)
                            HTMLReportGenerator.noCommentsOnChanges = true;
                        else if (level.compareTo("all") == 0) {
                            HTMLReportGenerator.noCommentsOnRemovals = true;
                            HTMLReportGenerator.noCommentsOnAdditions = true;
                            HTMLReportGenerator.noCommentsOnChanges = true;
                        }
                        return true;
                    }
                }
        );

        // Option to enable checking that the comments end with a period.

        options.add(
                new Option() {
                    private final List<String> names = List.of("-checkcomments");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() {
                        return "Check that comments are sentences";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        APIHandler.checkIsSentence = true;
                        return true;
                    }
                }
        );

        // Option to retain non-printing characters in comments.

        options.add(
                new Option() {
                    private final List<String> names = List.of("-retainnonprinting");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() {
                        return "Keep non-printable characters from comments.";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        RootDocToXML.stripNonPrintables = false;
                        return true;
                    }
                }
        );

        // Option for the name of the exclude tag

        options.add(
                new Option() {
                    private final List<String> names = List.of("-excludetag");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() {
                        return "Define the Javadoc tag which implies exclusion";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<tag>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        RootDocToXML.excludeTag = arguments.get(0);
                        RootDocToXML.excludeTag = RootDocToXML.excludeTag.trim();
                        RootDocToXML.doExclude = true;
                        return true;
                    }
                }
        );

        // Generate statistical output

        options.add(
                new Option() {
                    private final List<String> names = List.of("-stats");
                    @Override public int          getArgumentCount() { return 0; }
                    @Override public String       getDescription() {
                        return "Generate statistical output";
                    }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return ""; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.doStats = true;
                        return true;
                    }
                }
        );

        // Set the browser window title

        options.add(
                new Option() {
                    private final List<String> names = List.of("-windowtitle");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<title>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.windowTitle = arguments.get(0);
                        return true;
                    }
                }
        );

        // Set the report title

        options.add(
                new Option() {
                    private final List<String> names = List.of("-doctitle");
                    @Override public int          getArgumentCount() { return 1; }
                    @Override public String       getDescription() { return ""; }
                    @Override public Option.Kind  getKind() { return Option.Kind.STANDARD; }
                    @Override public List<String> getNames() { return names; }
                    @Override public String       getParameters() { return "<title>"; }
                    @Override public boolean      process(String opt, List<String> arguments) {
                        HTMLReportGenerator.docTitle = arguments.get(0);
                        return true;
                    }
                }
        );

        return options;
    }
}

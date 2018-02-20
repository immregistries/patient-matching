package org.openimmunizationsoftware.pm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openimmunizationsoftware.pm.matchers.AggregateMatchNode;
import org.openimmunizationsoftware.pm.matchers.MatchNode;

/**
 * Added by MIIS project. 
 * 
 */
public class PatientCompareMIIS {

  private String ancestry = "";
  private Date born = new Date();
  private AggregateMatchNode match;
  private AggregateMatchNode notMatch;
  private AggregateMatchNode twin;
  private AggregateMatchNode missing;

  public String makeScript() {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
    sb.append("Born:" + sdf.format(born) + ";");
    sb.append(match.makeScript());
    sb.append(";");
    sb.append(notMatch.makeScript());
    sb.append(";");
    sb.append(twin.makeScript());
    sb.append(";");
    sb.append(missing.makeScript());
    sb.append(";");
    return sb.toString();
  }

  public int readScript(String script) {
    int generation = 0;
    try {
      String[] parts = script.split("\\;");
      for (String part : parts) {
        if (part.startsWith("Born:")) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
          born = sdf.parse(readValue(part));
        } else if (part.startsWith("Match:")) {
          match.readScript(part);
        } else if (part.startsWith("Not Match:")) {
          notMatch.readScript(part);
        } else if (part.startsWith("Twin:")) {
          twin.readScript(part);
        } else if (part.startsWith("Missing:")) {
          missing.readScript(part);
        } else if (part.startsWith("Generation:")) {
          generation = Integer.parseInt(readValue(part));
        }
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to read script", e);
    }
    return generation;
  }

  private String readValue(String line) {
    int posColon = line.indexOf(":");
    if (posColon != -1) {
      String value = line.substring(posColon + 1).trim();
      return value;
    }
    return "";
  }

  public String getAncestry() {
    return ancestry;
  }

  public void setAncestry(String ancestry) {
    this.ancestry = ancestry;
  }

  public PatientCompareMIIS() {
    match = MatchNode.createPatientMatchNode();
    notMatch = MatchNode.createPatientNotMatchNode();
    twin = MatchNode.createTwinMatchNode();
    missing = MatchNode.createMissingMatchNode();

    // default
  }

  public void setMatchTestCase(MatchItem matchItem) {
    patientA = new Patient(matchItem.getPatientDataA());
    patientB = new Patient(matchItem.getPatientDataB());
    result = null;
  }

  public void clear() {
    patientA = null;
    patientB = null;
    result = null;
  }

  private Patient patientA = null;

  public AggregateMatchNode getMatch() {
    return match;
  }

  public void setMatch(AggregateMatchNode match) {
    this.match = match;
  }

  public AggregateMatchNode getNotMatch() {
    return notMatch;
  }

  public void setNotMatch(AggregateMatchNode notMatch) {
    this.notMatch = notMatch;
  }

  public AggregateMatchNode getTwin() {
    return twin;
  }

  public void setTwin(AggregateMatchNode twin) {
    this.twin = twin;
  }

  public String getSignature() {
    return match.getSignature(patientA, patientB) + notMatch.getSignature(patientA, patientB) + missing.getSignature(patientA, patientB)
        + twin.getSignature(patientA, patientB);
  }

  private static boolean USE_OLD_LOGIC = false;

  public String getResult() {
	  
	
    if (result == null) {
      if (USE_OLD_LOGIC) {
        double matchScore = match.weightScore(patientA, patientB);
        if (matchScore < 0.5) {
          matchScore = 0;
        }
        double notMatchScore = notMatch.weightScore(patientA, patientB);
        if (notMatchScore < 0.5) {
          notMatchScore = 0;
        }
        double preference = matchScore - notMatchScore;
        boolean matchSignal = match.hasSignal(patientA, patientB);
        boolean missingSignal = missing.hasSignal(patientA, patientB);
        boolean twinSignal = twin.hasSignal(patientA, patientB);
        if (matchSignal) {
          if (preference >= 0.2 && !missingSignal && !twinSignal) {
            result = "Match";
          } else {
            result = "Possible Match";
          }
        } else {
          result = "Not a Match";
        }
      } else {
        double matchScore = match.weightScore(patientA, patientB);
        if (matchScore < 0.5) {
          matchScore = 0;
        }
        double notMatchScore = notMatch.weightScore(patientA, patientB);
        if (notMatchScore < 0.5) {
          notMatchScore = 0;
        }

        boolean matchSignal = match.hasSignal(patientA, patientB);
        boolean notMatchSignal = notMatch.hasSignal(patientA, patientB);
        boolean missingSignal = missing.hasSignal(patientA, patientB);
        boolean twinSignal = twin.hasSignal(patientA, patientB);
        if (matchSignal) {
          if (notMatchSignal || missingSignal || twinSignal) {
            result = "Possible Match";
          } else {
            result = "Match";
          }
        } else {
          result = "Not a Match";
        }

      }
    }
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public AggregateMatchNode getMissing() {
    return missing;
  }

  public void setMissing(AggregateMatchNode missing) {
    this.missing = missing;
  }

  private String result = null;

  public Patient getPatientA() {
    return patientA;
  }

  public void setPatientA(Patient patientA) {
    this.patientA = patientA;
  }

  public Patient getPatientB() {
    return patientB;
  }

  public void setPatientB(Patient patientB) {
    this.patientB = patientB;
  }

  private Patient patientB = null;
  
  public static void main(String[] args) throws Exception{
	  
	  org.openimmunizationsoftware.pm.model.Patient    dedupPatientA = new Patient();
	  // populate the dedup patient hashmap
	  dedupPatientA.setNameFirst("First Name");
	  dedupPatientA.setNameLast("Last Name");


	  dedupPatientA.setBirthDate("1972.01.01");

	  dedupPatientA.setAddressStreet1("386 Main Street");
	  dedupPatientA.setAddressStreet2("Apt #2");

	  org.openimmunizationsoftware.pm.model.Patient dedupPatientB = new Patient();
      dedupPatientB = new Patient();
	  // populate the dedup patient hashmap
	  dedupPatientB.setNameFirst("First Name");
	  dedupPatientB.setNameLast("Last Name");
   

	  dedupPatientB.setBirthDate("1972.01.01");

	  dedupPatientB.setAddressStreet1("386 Main Street");
	  dedupPatientB.setAddressStreet2("Apt #2");
	  
	  AggregateMatchNode match;
	  AggregateMatchNode missing;
	  
	  match = MatchNode.createPatientMatchNode();
	  missing = MatchNode.createMissingMatchNode();
	  
	  double matchScore = match.weightScore(dedupPatientA, dedupPatientB);
	  System.out.print("Dedup match score = " + matchScore + "\n");
	  boolean missingSignal = missing.hasSignal(dedupPatientA, dedupPatientB);
	  
	  if (missingSignal)
		  System.out.println("This has a missing signal \n");

  }
	  
  
  
}

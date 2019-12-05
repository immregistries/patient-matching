package org.openimmunizationsoftware.pm;

import org.openimmunizationsoftware.pm.model.Patient;
import org.openimmunizationsoftware.pm.model.PatientCompare;

public class PatientMatcher {

  public static final String MATCHER_CONFIG_2015 =
      "Generation:1568;World Name:Year 2015;Island Name:Test1;Score:0.9011771147002464;Born:2012.01.12 07:39:31 MST;Match:0.5976539440304252:0.0::{Household:0.5340047021220341:0.0::{Last Name:0.7394477915760619:0.16083169281725096::{L-match:0.8975774524536817:0.0::}{L-similar:0.5769135677880574:0.0::}{L-hyphenated:0.08061266967349913:0.0::}}{Guardian:0.14758153486415468:0.0::{GF-match:0.1976740812001322:0.0::}{GL-match:0.2851225015188836:0.0::}{GF-GL-match:0.5:0.0::}{MM-match:0.17714551350571317:0.0::}{GL-MM-match:0.5:0.0::}}{Location:0.23618806521250102:0.0::{PN-match:0.18518135041561776:0.08589466214027962::}{AD-match:0.35703151393261645:0.21242201710004172::}}}{Person:0.6:0.0::{Patient Id:0.100348013698982:0.0::{MRN-match:0.12477428270001244:0.0::}{SSN-match:0.40396155335585626:0.0::}{MA-match:0.6544621737737066:0.0::}}{First Name:0.3:0.0::{F-match:0.9:0.0::}{F-similar:0.5:0.0::}{F-middle:0.9715935182634978:0.0::}{A-match:0.0:0.0::}{G-match:0.1:0.0::}}{Birth Order:0.1:0.01039887460808031::{BO-matches:0.19078632033502887:0.0::}{MBS-no:0.0:0.0::}}{DOB:0.4:0.0::{DOB-match:1.0:0.0::}{DOB-similar:0.25060457491471644:0.0::}}{Middle Name:0.3:0.09167939575959158::{M-match:0.6356853770678507:0.0::}{M-initial:0.07924819017444107:0.0::}{M-similar:0.6:0.0::}{S-match:0.0:0.0::}}{Shot History:0.2:0.0::{SH-match:0.1:0.0::}}};Not Match:0.44421419906866344:0.12240703311770455::{Household:0.21877320709099868:0.20163533642328246::{Last Name:0.4:0.0::{L-not-match:1.0:0.0:not:}{L-not-similar:0.607962842245958:0.18279269976421095:not:}{L-not-hyphenated:0.0:0.0:not:}}{Guardian:0.16971600294447498:0.12543614754336277::{GF-not-match:0.8772890703808285:0.0038207325234260736:not:}{GL-not-match:0.5:0.0:not:}{MM-not-match:0.6156030399580353:0.038992047203868174:not:}{MM-not-similar:0.530906191802011:0.2699050811578392:not:}}{Location:0.44207784318945:0.014988559786548339::{PN-not-match:0.0:0.0:not:}{AD-not-match:0.9:0.0:not:}}}{Person:0.0014557443002981328:0.0::{Patient Id:0.8:0.0::{MRN-not-match:0.4133203661483609:0.0:not:}{SSN-not-match:0.9:0.0:not:}{MA-not-match:0.6:0.0:not:}}{First Name:0.3:0.0::{F-not-match:0.09126893367169017:0.0:not:}{F-not-similar:0.02063213373726566:0.0:not:}{F-not-middle:0.0:0.0:not:}{A-not-match:0.0:0.0:not:}{G-not-match:0.1:0.0:not:}}{Birth Order:0.1:0.0::}{Gender:0.4:0.0::{G-not-match:0.0:0.0:not:}}{DOB:0.4:0.0::{DOB-not-match:0.043091496067329635:0.0:not:}{DOB-not-similar:0.4217422776328806:0.0:not:}}{Middle Name:0.3:0.0::{M-not-match:1.0:0.0:not:}{M-not-initial:0.6:0.0:not:}{M-not-similar:0.6:0.0:not:}{S-not-match:0.1:0.0:not:}}{Shot History:0.0:0.0::{SH-not-match:0.1:0.0:not:}}};Suspect Twin:1.0:0.0::{Name Different:0.31191437372224007:0.0::{F-not-match:0.5:0.0:not:}{F-not-similar:0.6:0.0:not:}{M-not-match:0.6824953258794706:0.435310546174943:not:}{G-not-match:0.5:0.19582951834359041:not:}}{Birth Date:0.2:0.0::{DOB-match:1.0:0.0::}}{Birth Status:0.6:0.0::{MBS-maybe:0.5:0.0023968922387398355::}{MBS-yes:0.1:0.0::}};Missing:1.0:0.0::{Household:0.37091729557143177:0.0::{L-missing:0.0:0.0::}{Household:0.4:0.0::{GFN-missing:0.25572373828186934:0.0::}{GLN-missing:0.19190311458033785:0.014397094425822488::}{MMN-missing:0.3:0.0::}}{Location:0.7208979096176452:0.0::{PN-missing:0.2:0.0::}{AS1-missing:0.0021417108647681904:0.0::}{AS2-missing:0.0:0.0::}{AC-missing:0.0:0.0::}{AS-missing:0.35029021967533147:0.0::}{AZ-missing:0.0:0.0::}}}{Person:0.6:0.026859283296303826::{Patient Id:0.3003400934691799:0.10261837790902595::{MRN-missing:0.9103716296607758:0.06301520760654365::}{SSN-missing:0.4:0.0:disabled:}{MA-missing:0.4:0.0:disabled:}}{First Name:0.3:0.0::{F-missing:1.0:0.0::}{A-missing:0.36654743978802545:0.0::}{S-missing:0.0:0.0::}{G-missing:0.4464521217969287:0.0::}}{Birth Order:0.1:0.0::{MBS-missing:1.0:0.0::}{BM-missing:0.5:0.0::}{BO-missing:0.6017422488947954:0.0::}}{DOB-missing:0.8342725880444734:0.20987242193211186::}{M-missing:0.2:0.0::}{SH-missing:0.18778690808745843:0.0::}};";

  private String matcherConfig;

  public PatientMatcher() {
    matcherConfig = MATCHER_CONFIG_2015;
  }

  public PatientMatcher(String matcherConfig) {
    this.matcherConfig = matcherConfig;
  }

  public PatientMatchDetermination match(Patient patientA, Patient patientB) {
    PatientCompare patientCompare = new PatientCompare();
    patientCompare.readScript(matcherConfig);
    patientCompare.setPatientA(patientA);
    patientCompare.setPatientB(patientB);
    String result = patientCompare.getResult();
    if (result.equals("Possible Match")) {
      return PatientMatchDetermination.POSSIBLE_MATCH;
    } else if (result.equals("Match")) {
      return PatientMatchDetermination.MATCH;
    }
    return PatientMatchDetermination.NO_MATCH;
  }
}

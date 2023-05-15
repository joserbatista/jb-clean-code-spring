package com.joserbatista.cleancode.service;

import com.joserbatista.cleancode.domain.Session;
import com.joserbatista.cleancode.domain.Speaker;
import com.joserbatista.cleancode.repository.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/// <summary>
/// Represents a single speaker
/// </summary>
@Service
public class SpeakerService {

    SpeakerRepository rep;

    @Autowired
    public SpeakerService(SpeakerRepository rep) {
        this.rep = rep;
    }

    /// <summary>
    /// register a speaker
    /// </summary>
    /// <returns>speakerID</returns>
    public Speaker register(Speaker speaker) throws NoSessionsApprovedException, SpeakerDoesntMeetRequirementsException, ArgumentNullException {
        //lets init some vars
        Speaker createdSpeaker = null;
        boolean good = false;
        boolean appr = false;

        //List<String> nt = Arrays.asList("MVC4", "Node.js", "CouchDB", "KendoUI", "Dapper", "Angular");
        List<String> ot = Arrays.asList("Cobol", "Punch Cards", "Commodore", "VBScript");

        //DEFECT #5274 DA 12/10/2012
        //We weren't filtering out the prodigy domain, so I added it.
        List<String> domains = Arrays.asList("aol.com", "hotmail.com", "prodigy.com", "CompuServe.com");

        if (StringUtils.hasLength(speaker.getFirstName())) {
            if (StringUtils.hasLength(speaker.getLastName())) {
                if (StringUtils.hasLength(speaker.getEmail())) {
                    //put list of employers in array
                    List<String> emps = Arrays.asList("Microsoft", "Google", "Fog Creek Software", "37Signals");

                    //DFCT #838 Jimmy
                    //We're now requiring 3 certifications so I changed the hard coded number. Boy, programming is hard.
                    good = ((speaker.getExp() > 10 || speaker.isHasBlog() || speaker.getCertifications().size() > 3 || emps.contains(
                        speaker.getEmployer())));

                    if (!good) {
                        //need to get just the domain from the email
                        String emailDomain = speaker.getEmail().split("@")[1];

                        if (!domains.contains(emailDomain) && (!("IE".equals(speaker.getBrowser().getName()) && 9 > speaker.getBrowser()
                                                                                                                           .getMajorVersion()))) {
                            good = true;
                        }
                    }

                    if (good) {
                        //DEFECT #5013 CO 1/12/2012
                        //We weren't requiring at least one session
                        if (speaker.getSessions().size() != 0) {
                            for (Session session : speaker.getSessions()) {

                                //for (String tech : ot) {
                                //    if (session.title.contains(tech)) {
                                //        session.approved = true;
                                //    }
                                //    break;
                                //}

                                for (String tech : ot) {

                                    if (session.getTitle().contains(tech) || session.getDescription().contains(tech)) {
                                        session.setApproved(false);
                                        break;
                                    } else {
                                        session.setApproved(true);
                                        appr = true;
                                    }
                                }
                            }
                        } else {
                            throw new ArgumentNullException("Can't register speaker with no sessions to present");
                        }

                        if (appr) {

                            //if we got this far, the speaker is approved
                            //let's go ahead and register him/her now.
                            //First, let's calculate the registration fee.
                            //More experienced speakers pay a lower fee.
                            if (speaker.getExp() <= 1) {
                                speaker.setRegistrationFee(500);
                            } else if (speaker.getExp() >= 2 && speaker.getExp() <= 3) {
                                speaker.setRegistrationFee(250);
                            } else if (speaker.getExp() >= 4 && speaker.getExp() <= 5) {
                                speaker.setRegistrationFee(100);
                            } else if (speaker.getExp() >= 6 && speaker.getExp() <= 9) {
                                speaker.setRegistrationFee(50);
                            } else {
                                speaker.setRegistrationFee(0);
                            }

                            //Now, save the speaker and sessions to the db.
                            try {
                                createdSpeaker = rep.save(speaker);
                            } catch (Exception e) {
                                //in case the db call fails
                            }
                        } else {
                            throw new NoSessionsApprovedException("No sessions approved");
                        }
                    } else {
                        throw new SpeakerDoesntMeetRequirementsException("Speaker doesn't meet our arbitrary and capricious standards");
                    }

                } else {
                    throw new ArgumentNullException("Email is required");
                }
            } else {
                throw new ArgumentNullException("Last name is required");
            }
        } else {
            throw new ArgumentNullException("First Name is required");
        }

        //if we got this far, the speaker is registered.
        return createdSpeaker;
    }

    public Speaker findById(final Long id) {
        return rep.get(id);
    }

    //region Custom Exceptions
    public class SpeakerDoesntMeetRequirementsException extends Exception {

        public SpeakerDoesntMeetRequirementsException(String message) {
            super(message);
        }
    }


    public class NoSessionsApprovedException extends Exception {

        public NoSessionsApprovedException(String message) {
            super(message);
        }
    }


    public class ArgumentNullException extends Exception {

        public ArgumentNullException(String message) {
            super(message);
        }
    }
    //endregion
}
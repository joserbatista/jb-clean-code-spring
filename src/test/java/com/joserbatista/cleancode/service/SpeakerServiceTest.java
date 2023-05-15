package com.joserbatista.cleancode.service;

import com.joserbatista.cleancode.domain.Session;
import com.joserbatista.cleancode.domain.Speaker;
import com.joserbatista.cleancode.domain.WebBrowser;
import com.joserbatista.cleancode.repository.DummySpeakerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SpeakerServiceTest {

    //Hard coding to single concrete implementation for simplicity here.
    private final SpeakerService service = new SpeakerService(new DummySpeakerRepository());

    @Test
    void register_EmptyFirstName_ThrowsArgumentNullException() {
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setFirstName("");

        Assertions.assertThatExceptionOfType(SpeakerService.ArgumentNullException.class).isThrownBy(() -> this.service.register(speaker))
                  .withMessage("First Name is required");
    }

    @Test
    void register_EmptyLastName_ThrowsArgumentNullException() {
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setLastName("");

        Assertions.assertThatExceptionOfType(SpeakerService.ArgumentNullException.class).isThrownBy(() -> this.service.register(speaker))
                  .withMessage("Last name is required");
    }

    @Test
    void register_EmptyEmail_ThrowsArgumentNullException() {

        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setEmail("");

        Assertions.assertThatExceptionOfType(SpeakerService.ArgumentNullException.class).isThrownBy(() -> this.service.register(speaker))
                  .withMessage("Email is required");
    }

    @Test
    void register_WorksForPrestigiousEmployerButHasRedFlags_ReturnsSpeakerId()
        throws Exception {
        //arrange
        Speaker speaker = getSpeakerWithRedFlags();
        speaker.setEmployer("Microsoft");

        //act
        Speaker speakerId = this.service.register(speaker);

        //assert
        Assertions.assertThat(speakerId).isNotNull();
    }

    @Test
    void register_HasBlogButHasRedFlags_ReturnsSpeakerId()
        throws Exception {
        //arrange
        Speaker speaker = getSpeakerWithRedFlags();

        Speaker speakerId = this.service.register(speaker);

        //assert
        Assertions.assertThat(speakerId).isNotNull();
    }

    @Test
    void register_HasCertificationsButHasRedFlags_ReturnsSpeakerId()
        throws Exception {
        //arrange
        Speaker speaker = getSpeakerWithRedFlags();
        speaker.setCertifications(List.of("cert1", "cert2", "cert3", "cert4"));

        Speaker speakerId = this.service.register(speaker);

        //assert
        Assertions.assertThat(speakerId).isNotNull();
    }

    @Test
    void register_SingleSessionThatIsOnOldTech_ThrowsNoSessionsApprovedException() {
        //arrange
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setSessions(List.of(Session.builder().title("Cobol for dummies").description("Intro to Cobol").build()));

        Assertions.assertThatExceptionOfType(SpeakerService.NoSessionsApprovedException.class)
                  .isThrownBy(() -> this.service.register(speaker));
    }

    @Test
    void register_NoSessionsPassed_ThrowsArgumentException() {
        //arrange
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setSessions(Collections.emptyList());

        Assertions.assertThatExceptionOfType(SpeakerService.ArgumentNullException.class)
                  .isThrownBy(() -> this.service.register(speaker))
                  .withMessage("Can't register speaker with no sessions to present");
    }

    @Test
    void register_DoesntAppearExceptionalAndUsingOldBrowser_ThrowsNoSessionsApprovedException() {
        //arrange
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setHasBlog(false);
        speaker.setBrowser(WebBrowser.builder().name("IE").majorVersion(6).build());

        Assertions.assertThatExceptionOfType(SpeakerService.SpeakerDoesntMeetRequirementsException.class)
                  .isThrownBy(() -> this.service.register(speaker))
                  .withMessage("Speaker doesn't meet our arbitrary and capricious standards");
    }

    @Test
    void register_DoesntAppearExceptionalAndHasAncientEmail_ThrowsNoSessionsApprovedException() {
        //arrange
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setHasBlog(false);
        speaker.setEmail("name@aol.com");

        Assertions.assertThatExceptionOfType(SpeakerService.SpeakerDoesntMeetRequirementsException.class)
                  .isThrownBy(() -> this.service.register(speaker))
                  .withMessage("Speaker doesn't meet our arbitrary and capricious standards");
    }

    //region Helpers
    private static Speaker getSpeakerThatWouldBeApproved() {
        Speaker speaker = new Speaker();
        speaker.setFirstName("First");
        speaker.setLastName("Last");
        speaker.setEmail("example@domain.com");
        speaker.setEmployer("Example Employer");
        speaker.setHasBlog(true);
        speaker.setBrowser(new WebBrowser("test", 1));

        speaker.setExp(1);
        speaker.setCertifications(new ArrayList<>());
        speaker.setBlogURL("");
        speaker.setSessions(List.of(new Session("test title", "test description", false)));
        return speaker;
    }

    private static Speaker getSpeakerWithRedFlags() {
        Speaker speaker = getSpeakerThatWouldBeApproved();
        speaker.setEmail("tom@aol.com");
        speaker.setBrowser(new WebBrowser("IE", 6));
        return speaker;
    }
    //endregion
}
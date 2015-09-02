package org.jbehave.core.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.StepPattern;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.steps.StepFinder.ByDeclaringClass;

public class ByDeclaringClassPrioritisingStrategy extends JUnitStory {

    @Override
    public Configuration configuration() {
        final ByDeclaringClass priorityStrategy = new ByDeclaringClass();
        final StepFinder stepFinder = new StepFinder(priorityStrategy);
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withFormats(Format.CONSOLE))
                .useStepMonitor(priorityStrategy)
                .useStepFinder(stepFinder)
                .useStepCollector(new MarkUnmatchedStepsAsPending(stepFinder) {
                    /*
                     * Temporary hack so our prioritizing strategy can be notified with step events,
                     * I didn't found a way to to this properly.
                     */
                    @Override
                    public List<Step> collectScenarioSteps(List<CandidateSteps> candidateSteps, Scenario scenario, Map<String, String> parameters,
                            StepMonitor stepMonitor) {
                        return super.collectScenarioSteps(candidateSteps, scenario, parameters, new DelegatingStepMonitor(stepMonitor) {
                            @Override
                            public void stepMatchesPattern(String step, boolean matches, StepPattern stepPattern, Method method, Object stepsInstance) {
                                priorityStrategy.stepMatchesPattern(step, matches, stepPattern, method, stepsInstance);
                                super.stepMatchesPattern(step, matches, stepPattern, method, stepsInstance);
                            }

                            @Override
                            public void stepMatchesType(String stepAsString, String previousAsString, boolean matchesType, StepType stepType, Method method,
                                    Object stepsInstance) {
                                priorityStrategy.stepMatchesType(stepAsString, previousAsString, matchesType, stepType, method, stepsInstance);
                                super.stepMatchesType(stepAsString, previousAsString, matchesType, stepType, method, stepsInstance);
                            }
                        });
                    }
                });
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new PaperMailSteps(), new EMailSteps());
    }

    public static class PaperMailSteps {

        private final Map<String, String> mailboxes = new HashMap<String, String>();

        private List<String> recipients;
        private String content;

        @Given("I use a paper and a pen")
        @When("I use a paper and a pen")
        public void usePaperAndPen() {
        }

        @Given("I write $content")
        public void writePaperMail(final String content) {
            this.content = content;
        }

        @Given("I add $recipient to the recipients list")
        public void addRecipient(final String recipient) {
            if (null == recipients) {
                recipients = new ArrayList<String>();
            }
            recipients.add(recipient);
        }

        @When("I send the message")
        public void sendPaperMail() {
            for (final String recipient : recipients) {
                mailboxes.put(recipient, content);
            }
        }

        @Then("$recipient 's paper inbox is empty")
        public void verifyEmptyInbox(final String recipient) {
            final String mailboxContent = mailboxes.get(recipient);
            assertThat(mailboxContent, nullValue());
        }

        @Then("$recipient 's paper inbox contains $expectedContent")
        public void verifyMailContent(final String recipient, final String expectedContent) {
            final String mailboxContent = mailboxes.get(recipient);
            assertThat(mailboxContent, equalTo(expectedContent));
        }
    }

    public static class EMailSteps {

        private final Map<String, String> mailboxes = new HashMap<String, String>();

        private List<String> recipients;
        private String content;

        @Given("I use a computer")
        @When("I use a computer")
        public void useComputer() {
        }

        @Given("I write $content")
        public void writePaperMail(final String content) {
            this.content = content;
        }

        @Given("I add $recipient to the recipients list")
        public void addRecipient(final String recipient) {
            if (null == recipients) {
                recipients = new ArrayList<String>();
            }
            recipients.add(recipient);
        }

        @When("I send the message")
        public void sendPaperMail() {
            for (final String recipient : recipients) {
                mailboxes.put(recipient, content);
            }
        }

        @Then("$recipient 's email inbox is empty")
        public void verifyEmptyInbox(final String recipient) {
            final String mailboxContent = mailboxes.get(recipient);
            assertThat(mailboxContent, nullValue());
        }

        @Then("$recipient 's email inbox contains $expectedContent")
        public void verifyMailContent(final String recipient, final String expectedContent) {
            final String mailboxContent = mailboxes.get(recipient);
            assertThat(mailboxContent, equalTo(expectedContent));
        }
    }
}

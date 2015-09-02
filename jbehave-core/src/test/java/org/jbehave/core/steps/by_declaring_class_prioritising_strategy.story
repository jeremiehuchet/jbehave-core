Prioritise StepCandidates declared in the same class as the previous step

Narrative:
In order to solve step name conflicts
As a developer
I want the StepFinder to choose the StepCandidate declared in the same class of the last performed step

Scenario: I can send a paper mail to Bob using confusing steps
Given I use a paper and a pen
And I write
>>> Hello! This is a PAPER mail
And I add Bob to the recipients list
When I send the message
Then Bob 's paper inbox contains
>>> Hello! This is a PAPER mail

Scenario: I can send an email to Alice using confusing steps
Given I use a computer
And I write
>>> Hello! This is an E-mail
And I add Alice to the recipients list
When I send the message
Then Alice 's email inbox contains
>>> Hello! This is an E-mail

Scenario: I can alternate between using "paper mail" steps and "email" steps
Given I use a paper and a pen
And I write
>>> Hello! This is a PAPER mail

Given I use a computer
And I write
>>> Hello! This is an E-mail

Given I use a paper and a pen
And I add Jen to the recipients list

Given I use a computer
And I add Paul to the recipients list

Given I use a paper and a pen
And I add Jessie to the recipients list

Given I use a computer
And I add Pascal to the recipients list

When I use a computer
And I send the message

Then Jen 's paper inbox is empty
And Jessie 's paper inbox is empty
And Paul 's email inbox contains
>>> Hello! This is an E-mail
And Pascal 's email inbox contains
>>> Hello! This is an E-mail

When I use a paper and a pen
And I send the message

Then Jen 's paper inbox contains
>>> Hello! This is a PAPER mail
And Jessie 's paper inbox contains
>>> Hello! This is a PAPER mail
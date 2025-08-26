# Paurus Task #1

Job application first task to resolve.

## Task description:

~~~
Taxation is a term for when a taxing authority (usually a government) imposes a tax. Imagine having multiple web sites in many countries (let's call these sites traders) out of which each must follow different local taxation rules and calculations. Site users bets an amount on the give odd, e.g. 5 EUR * 1.5 = 7.5 EUR. Everything will be done in EUR currency to keep it simple.

Prepare a REST service with specified input/output:

"incoming": {
"traderId": 1,
"playedAmount": 5,
"odd": 3.2
}

"outgoing" {
"possibleReturnAmount": Q,
"possibleReturnAmountBefTax": W,
"possibleReturnAmountAfterTax": X,
"taxRate": Y,
"taxAmount" Z
}

Your service must support two types of taxation:

1. General
    - taxation is done per rate or per amount:

rate: 10%
7.5EUR * 0.1 = 0.75EUR => possible return amount is 7.5EUR - 0.75EUR = 6.75
amount: 2EUR
7.5EUR - 2EUR = 5.5EUR => possible return amount is 5.5EUR
2. Winnings
    - winnings amount: 7.5EUR - 5EUR = 2.5EUR
    - taxation is done per rate or per amount:

rate: 10%
2.5EUR * 0.1 = 0.25EUR => possible return amount is 7.5EUR - 0.25EUR = 7.25EUR
amount: 1EUR
2.5EUR - 1EUR = 1.5EUR => possible return amount is 1.5EUR
Since your doing a REST service you don't have to bother with UI. The correctness of implementation can be shown with unit/integration tests or Postman (www.getpostman.com) calls. Technologies: JEE, Spring, Spring Boot, Maven, etc. How data is stored - that's up to you :)
~~~

### Prerequisites

- Java 21
- Spring boot 3.5.5
- Maven (wrapper provided)

### Run instructions

- IntelliJ:
    - IntelliJ should detect it as maven project and download dependencies
    - TaskoneApplication.java right click run (check that you have project SDK set to 21)
    - app will start on localhost:8080
    - http://localhost:8080/actuator/health should show status up.
    - swagger is available on http://localhost:8080/swagger-ui/index.html

CLI:

~~~
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
~~~

### Example requests

Example from instructions

~~~
{
    "traderId": 1,
    "playedAmount": 5,
    "odd": 3.2
}
~~~

Invalid traderId validation error

~~~
{
    "traderId": 3,
    "playedAmount": 5,
    "odd": 3.2
}
~~~

Invalid odd validation error

~~~
{
    "traderId": 1,
    "playedAmount": 5,
    "odd": 0.5
}
~~~

Negative winnings validation error

~~~
{
    "traderId": 1,
    "playedAmount": -5,
    "odd": 1.5
}
~~~

Request example where 10% taxation is used. If taxationType GENERAL was used
possibleReturnAmountAfterTax should be 900. If WINNINGS then 910.

~~~
{
    "traderId": 1,
    "playedAmount": 100,
    "odd": 10
}
~~~

### Clarification, thought process

- Why Java 21: 24 not LTS, possible compatibility issues with Spring Boot.
- Why not spring boot 4. There is only preview version.
- No database. Since DB is required in task #2, rules will be provided in properties.
- No authorization. Not specified in instructions.
- I would usually format commit messages with task id, short message and longer description bellow. I'll only write
  short msg here. 
- I don't know if I should do KISS/straight to the point or add some potentially useful implementation.
    - EDIT: I've added validation and exception handling. Ccould do interface for validation if more than one validator
      was done. Same for mapper.
- Would change package hierarchy if service was complex. Ask if this is something you want an elaboration on.
- Two approaches for implementing taxation service. One would be service for each government. One service for all
  governments. That would come from talking to architect/analytic which would give solution based on extended business
  information they have.
    - Edit: I'm such a fool. TraderId determines the website which determines parameters. Sigh.
- Need input on how taxation type (general, winnings) is determined and how taxation (rate, amount) is determined. For
  now I am leaving it as request parameter and that they are determined in application properties.
- It really would make more sense to determine type and mode behind the scene and not as a parameter. This really need
  clarification in order to be done correctly.
    - Edit: I see that you always provide both modes as a result.
- Would need clarification about precision. How big should precision be. I've put precision to 2. But I expect that it
  could also be more and is accumulated through time and that two would only be mapped when withdrawing.
- I could not deduce how possibleReturnAmount differs from possibleReturnAmountBefTax.
- Repeating calculateWinnings calling in BetMapper because of importance of this calculation. If one time call would be
  implemented then i would have some other mechanisms to warn/remind me in case of code change.
- I'm going to assume that odd must always be above 1?
- there is a commented out @Valid if you want to switch to jakarta validation. If you put null in NewBetBO there will be
  a 500, which could be handled this way (or in validation). 500 returns uuid, which can be traced to log entry to find
  error description.

### TODO:

- [ ] Write integration tests
- [ ] Write MoneyUtil tests
- [x] complete readme (run instructions and example requests) 
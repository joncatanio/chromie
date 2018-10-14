# Chromie
<img src="resources/images/chromie.jpeg" height="200px">

Chromie is a [Spring Boot](https://spring.io/projects/spring-boot) application that distributes karma to members of a Slack group.
The application uses [JBot](https://github.com/rampatra/jbot) as a dependency which establishes a connection with Slack
while also providing a number of helpful classes to ease the communication with the API.
[Chromie](https://www.wowhead.com/npc=10667/chromie) is an homage to the character in World of Warcraft and the successor
to our previous karma bot [Jeeves](https://github.com/joncatanio/jeeves).

## Building Locally
Getting a local build of Chromie is a simple as cloning the repository and running:

```
mvn clean install
```

This will download all the [Maven](https://maven.apache.org/) dependencies and install
them into the local `~/.m2` directory, compile the project, and run the unit tests.

## Running Locally
The `pom.xml` file includes an [h2 database](https://github.com/h2database/h2database)
dependency for testing so setting up a local database isn't necessary. An
[`application-qa.properties`](src/main/resources/application-qa.properties)
file is included in the project that has Spring datasource properties that may
be changed to reference a local database instance.

The following command will run the application with the `slack` and `qa` profiles active:

```
mvn spring-boot:run -Dspring.profiles.active=slack,qa
```

Drop the `qa` profile to run Chromie with the in-memory h2 database instance.

## Deploying
Chromie is currently deployed to [Heroku](https://www.heroku.com/) with a
[JawsDB Maria](https://elements.heroku.com/addons/jawsdb-maria) addon configured.
A [Procfile](Procfile) is included with a web process type configured to run
the application with `slack` and [`prod`](/src/main/resources/application-prod.properties)
properties. More information about how Heroku uses the Procfile may be found
[here](https://devcenter.heroku.com/articles/procfile).

If a Heroku app hasn't been created then one may do so through the CLI.

1. Get the [Heroku toolbelt](https://devcenter.heroku.com/articles/heroku-cli)
   * On Macbooks with [Homebrew](https://brew.sh/) it's as simple as `brew install heroku/brew/heroku`
2. `$ heroku login` - Log into Heroku 
3. `$ heroku create <app-name>` - Create app with or without an app name
   * This adds a new remote to the git repo, run `git remote -v` to see the new `heroku` remote
4. `$ git push heroku master` - Push the code to Heroku

We now have a Heroku app with our code ready to be run. Start the application by executing the following
command in the project's root directory:

```
$ heroku ps:scale web=1
```

This will scale the number of `web` [dynos](https://devcenter.heroku.com/articles/dynos) to 1. Setting `web=0`
will scale the dynos to 0 resulting in the application stopping.

Restarting the application can be done with the following command:

```
$ heroku restart -a <app-name>
```

## Configuring Slack Bot
1. Get a [Slack Bot token](https://my.slack.com/services/new/bot)
2. Update the `slackBotToken` property in the [application.properties](src/main/resources/application.properties) file

The Slack Bot token shouldn't be shared or exposed publicly. One can externalize the properties with Heroku by going to
the application's settings in the Heroku dashboard and setting up a new [Config Var](https://devcenter.heroku.com/articles/config-vars).
Or setting it through the CLI with `heroku config:set <CONFIG_VAR_NAME>=<value>`.

Heroku will then deploy the application with these environment variables set and they may be accessed in the properties file
by using `${}`, e.g. `slackBotToken=${SLACK_BOT_TOKEN}`. 

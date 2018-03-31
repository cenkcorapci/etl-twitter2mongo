# etl-twitter2mongo
Simple etl tool for extracting twitter data set from [Cheng-Caverlee-Lee September 2009 - January 2010 Twitter Scrape](https://archive.org/details/twitter_cikm_2010)
and load to [MongoDB](https://www.mongodb.com/).

## Configuration
This project uses environment variables for configuration.Here are environment variables
that can be used for configuration.[TypeSafe Config](https://github.com/lightbend/config)
has been used for this purpose, so you can find configuration details in `application.conf` file.


| Env Variable   |      Usage      |
|----------|:-------------:|
| DATA_FOLDER |  Path of the folder that contains data |
| TEST_USERS_FILE_NAME |    name of users file for test   |
| TRAINING_USERS_FILE_NAME | name of users file for training |
| TEST_TWEETS_FILE_NAME | name of tweets file for test |
| TRAINING_TWEETS_FILE_NAME | name of tweets file for training |
| MONGO_URL | connection url to Mongo DB |
| DEFAULT_PARALLELISM | How many actors will be useed in parallel for processing the data |

## Building from sources
[sbt-assembly](https://github.com/sbt/sbt-assembly) plugin has been added, so you can
create a fat-jar with `sbt assembly` command for deployment.
### Running
After setting the environment variables or editing the `application.conf`, just type `sbt run`.
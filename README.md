# CSYE7200_Dynamic_Movie_Recommendation
## Introduction

A movie recommendation system dynamically provides users with personalized recommendations based on their rating data for movies. 

- Software and Version:
   - Scala version: 2.12.15
   - Java version: 11
   - sbt version: 1.3.13
   - Kafka version: 3.0.0
   - Spark version: 3.2.0
   - Redis version: 3.2
   - Hadoop version: 3.3.1
   - MySQL version: 5.6
   - Play version: 5.0.0
   - Grafana version: 8.2.4
   
- Datasource: 
   - small dataset
      - https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/main/dataflow-stimulator/src/main/resources/ratings.csv
      - https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/db-utils/src/main/resources/movies.csv
   - big dataset
      - https://files.grouplens.org/datasets/movielens/ml-25m-README.html
      - to run the big dataset, you will need more computer resources (at least 32G memory and 16 cores CPU)
- Slides: 
   - proposal https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/Presentation%20Slides%20(both%20slides%20goes%20here)/final%20project%20proposal%20(DMRS).docx
   - planning presentation slides: https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/Presentation%20Slides%20(both%20slides%20goes%20here)/CSYE7200%20-%20Planning%20Presentation.pptx
   - final slides: https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/Presentation%20Slides%20(both%20slides%20goes%20here)/CSYE7200%20-%20%20Final%20Presentation.pptx

## How to run the project <a id="run"/>
> Note:  Because play framework uses some plugins that IntelliJ could not recognize, you MUST use terminal to run the whole project. Like 'sbt run'.  Otherwise, you will see errors in IntelliJ.

#### Step 1: Install & Start Servers

   1. Ubuntu 20.04 LTS----for windows os
   2. Java 11
   3. Scala 2.12.15
   4. Kafka 3.0.0
   5. sbt 1.3.13
   6. Redis 3.2
   7. Hadoop 3.3.1
   8. MySQL 5.6
   9. Grafana 8.2.4

#### Step 2: Start Application

   10. <a href="#dataflow & etl">Dataflow Simulator & ETL</a> <br/>
   11. <a href="#algorithms">Algorithms</a><br/>
   12. <a href="#UI"> User Interface (Play) <br/>
   13. <a href="#data visualization">Data Visualization (MySQL & Grafana)</a> <br/>


## ---------------------Step1: Install & Start Servers---------------------

## Ubuntu 20.04 LTS---for windows os
  * get Ubuntu from app store
  * if your pc is Linux OS or Mac OS, you can skip this step.

## Java 11
  * install jre: ```sudo apt install default-jdk ```
  * install compiler: ```sudo apt-get install openjdk-11-jdk```
  * check java version: java -version (jre) javac -version (compiler)

## Scala 2.12.15  
  * install scala
  ```
  sudo apt-get update
  sudo apt-get install scala
  ```
  * check Scala version: ```scala -version```

## Kafka 3.0.0
  * install kafka:

   ```
   wget https://dlcdn.apache.org/kafka/3.0.0/kafka_2.13-3.0.0.tgz
   tar xzf kafka_2.13-3.0.0.tgz
   cd kafka_2.13-3.0.0
   ```

* set configurations: 

  * go to path=db-utils/src/main/resources/application.properties
  * change kafka.server to your localhost

* start Kafka: 

     * start zookeeper first
         * go to /kafka
         * run ```bin/zookeeper-server-start.sh config/zookeeper.properties```
     * then start kafka 
         * go to /kafka
         * run ```bin/kafka-server-start.sh config/server.properties```
         * if kafka shuts down, restart it.
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Screen Shot 2023-01-31 at 2.38.43 PM.png" width=80%></p>
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/kafka2.png" width=65%></p>


## sbt 1.3.13

   * install sbt
     * follow the offical document of scala and sbt to set up and use sbt by cmd: <br/>
       https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html https://www.scala-sbt.org/1.x/docs/sbt-by-example.html

```
sudo apt-get update
sudo apt-get install apt-transport-https curl gnupg -yqq
echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo -H gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import
sudo chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg
sudo apt-get update
sudo apt-get install sbt
```


 ## Redis 3.2

* install Redis

```shell
# download and install
wget https://download.redis.io/releases/redis-6.2.6.tar.gz
tar xvzf redis-6.2.6.tar.gz
cd redis-6.2.6/
make
  
# start redis
src/redis-server
```

* set configurations

  * go to path=db-utils/src/main/resources/application.properties
  * change redis.server, redis.port according where you install your redis, and its port; Typically, redis.server is the ip address of your redis, and port in default is 6379

* Note: your redis must run in the same machine as the Movie Recommendation Application

## Hadoop 3.3.1

> You can follow this link to install Hadoop https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html#Download

- download Hadoop
```shell
wget https://www.apache.org/dyn/closer.cgi/hadoop/common/hadoop-3.3.1/hadoop-3.3.1.tar.gz
```

* install Hadoop
```shell
# set to the root of your Java installation
export JAVA_HOME=/usr/java/latest
```



## MySQL 5.6

> you can follow this link to install mysql https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-18-04

* install & start MySQL

```
sudo apt update
sudo apt install mysql-server
sudo service mysql start
sudo mysql_secure_installation
```

* change root password
   
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/m5.png" width=50%></p>

* create database  
```sql
CREATE DATABASE `recsys` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
```
  
* create tables
```sql
# choose database
use recsys
   
CREATE TABLE `input` (
`amount` int(11) DEFAULT '0',
`time` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
   
CREATE TABLE `performance` (
`rmse` double DEFAULT NULL,
`accuracy` double DEFAULT NULL,
`time` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/m1.png" width=55%></p>
   
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/m2.png" width=85%></p>
   
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/m3.png" width=75%></p>
   
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/m4.png" width=55%></p>

   
If you can see table `input` and `performance` is created as picture showed, then you create table successfully.

* set configurations 
  * go to path=db-utils/src/main/resources/application.properties
  * change mysql.server, mysql.username, mysql.password

## Grafana 8.2.4
   * install and start Grafana: <br/>
     https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-grafana-on-ubuntu-20-04

```shell
wget -q -O - https://packages.grafana.com/gpg.key | sudo apt-key add -

sudo add-apt-repository "deb https://packages.grafana.com/oss/deb stable main"

sudo apt update

sudo apt install grafana

sudo systemctl start grafana-server

sudo systemctl status grafana-server

```

ou will receive output similar to this:

```
Output grafana-server.service - Grafana instance
   Loaded: loaded (/lib/systemd/system/grafana-server.service; disabled; vendor preset: enabled)
   Active: active (running) since Thu 2020-05-21 08:08:10 UTC; 4s ago
   Docs: http://docs.grafana.org
   Main PID: 15982 (grafana-server)
   Tasks: 7 (limit: 1137)
...
```



   * open Grafana client 
      * visit http://localhost:3000 if you don't change the port, it should be 3000

- config Grafana datasource
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g1.png" width=75%></p>
  
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g2.png" width=85%></p>
  
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g3.png" width=85%></p>
  
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g4.png" width=50%></p>
  
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g5.png" width=45%></p>
  
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g6.png" width=50%></p>
  
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/g7.png" width=95%></p>


- import Grafana dashboard

  Click 'upload JSON File' to select the import.json, and the click 'Load'. Then you will see the dashboard.



<a href="#run">Back to Index</a> <br/>

## ------------------Start Application------------------

## Package configration ##

* double check the configuration
   configuration file path: `db-utils/src/main/resources/application.properties`
   , check if the configuration is right according your own environment. If it is not correct, change it, then repackage it, and copy it to every module's `lib` directory. (**Very Important**)
 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/config_properties.png" width=80%></p>

* package db-utils
  

 ```shell
 cd db-utils
 sbt package
 ```

* copy the packaged jar (`db-utils_2.12-0.1.jar` in `target/scala-2.12/` directory) to where need this configuration
  

 (1) dataflow-stimulator
   path: dataflow-stimulator/lib

 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/dataflow_jar.png" width=30% ></p>

 (2) ETL
   path:ETL/lib

 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/ETL_jar.png" width=30%></p>

 (3) movie-recommed
   path: movie-recommend/lib

 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/movie_recommend_jar.png" width=30%></p>

 (4) movie-web
   path: movie-web/lib

 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/movie_web_jar.png" width=30%></p>

## Dataflow Simulator & ETL <a id="dataflow & etl"/>
### Outline:
1. Run ETL Process - use shell
2. Run Dataflow-Simulator
3. Check checkpoint
4. Check CSV Result

### 1. Run ETL Process

* ensure Kafka is running correctly as the previous step
* cd ../CSYE7200-MovieRecommendation/ETL, input sbt run
  
 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/sbt_run.png" width=80%></p>
* check console
   * If the root result appears, and no error is reported, it means the execution is successful
   
 <p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Run_ETL_Success.png" width=80%></p>

### 2. run Dataflow-Simulator
* go to /dataflow-simulator, run the following command
```
sbt compile
sbt run, and then select MyProducer.scala
```
<p><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/producer.png" width=80%></p> <br/>
<p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Kafka_Prodcuer_Success.png" width=60%></p>

### 3. Check Console
  * If the batch in the console received no data, just like the picture below, that means all of the data in kafka has been consumed. Then we need restart kafka producer. Please refer to formal step in dataflow-stimulator module to rerun kafka producer.  
    
<p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/bath0.png" width=40%></p>
  * If the batch in the console received data, just like the picture below, that means ETL process successful, and data will save into CSV files. Every batch represents one csv file
    
<p align="left"><img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/bath2.png" width=30%></p>
 * After the previous step is executed successfully, do not close this shell window.
   

### 4. Check checkpoint

Checkpoint helps build fault-tolerant and resilient Spark applications. In Spark Structured Streaming, it maintains intermediate state on HDFS compatible file systems to recover from failures. To specify the checkpoint in a streaming query, we use the checkpointLocation parameter.

After run the ETL module, we can see the checkpoint folder appear in the ETL directory.

<p align="left">
<img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/checkpoint.png" width=40%>
</p>

### 5. Check CSV Result

In the ETL module, the data file undergoes a series of transformations and is finally saved as a CSV file.

Main process：

(1) read raw data from kafka

schema：
<p align="left">
<img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Kafka_schema.png" width=45% >
</p>


(2）take value convert to Dataset[RatingLog]

schema：
<p align="left">
<img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Dataset[RatingLog]_schema.png" width=40%>
</p>


(3）save to csv - Final Result

This is the final result. We can see ETLRating folder appears in the ETL directory

<p align="left">
<img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Final_csv.png" width = "400" height="300">
</p>
   
<a href="#run">Back to Index</a> <br/>




## Start Movie-Recommend module

This module has two Apps: ALSRecommendApp, StatisticRecommendApp. We should start them one by one in different terimals.

This module is to train the ML model with ALS algorithm, and use statistical knowledge to get top N movies from all movies rating dataset, and top N movies for each genre.

```shell
# start ALSRecommendApp
cd movie-recommend
sbt run
choose [1] edu.neu.movierecommend.ALSRecommendApp

# start StatisticRecommendApp$
cd movie-recommend
sbt run
choose [2] edu.neu.movierecommend.StatisticRecommendApp
```



## Start movie-web module

```shell
# start
cd movie-web
sbt run

# open web page
http://localhost:9000
```







<a href="#run">Back to Index</a> <br/>


## User Interface <a id="UI"/>
### Outline
   1. UI set up
   2. UI flow

### 1. UI Set up (Play framework)
-Play framework 2.8.x or 2.6.x
- cd movie-web
- make sure to add these config in plugins.sbt (under project folder):
```
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")
```
- sbt reload
- sbt run
- Open Play application on localhost:9000

### 2. UI flow
   - Home page:
<p align="left">
<img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/Homepage.png" width=80%>
</p>

   * In here user will enter their id, their request movies number (anywhere from 1-20 or 30 should work),and other movies for each genre that they want to be displayed. For example, input userid = 1, recommendNum=10, genreNum=6, then `submit`.

   - Recommend Page:
<p align="left">
<img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/RecommendPage.png" width=80%>
</p>

      * Recommend page will have list of movies with system predicted rating suggested to user,as well as other genes movies like Action, Comedy, Horror, etc
      * Click on image/movies title will bring user to rating page of that movies, click on genre title will bring them to IMDB list of movies related of that genre

   - Rating Page
      * User will submit their id, movie id as well as their movie rating (whole number from 1-5). Afterward, they are brought back to home page.

   <a href="#run">Back to Index</a> <br/>





## Algorithms <a id="algorithms"/>

### Define the problem

The most abundant features we can get from dataset is user's rating. After convert it into a user-movie matrix, it is a sparse matrix because most of users only rate a small number of movies. We can not directly use the traditional ML models, like KNN, SVM to implement recommendation.

However, if we can find a way to predict ratings of every user on all movies, we are able to recommend user the movies with highest ratings. 

### Solutions/methods

So our first job is to define the Loss Function, which is the mean square root of error (error is actual rating and predicted rating). Then use ALS or Gradient Descent to find the best prediction by decreasing the value of Loss Function.

With Cross-Validation, we are able to avoid overfitting and efficiently test  our model. We also use Grid Search to tune hyper parameter in ALS. 

In the end, we get a matrix which has predicted ratings for every user on all movies. With this matrix, we are able to find the best rating movies for specific user and recommend these movies to this user.

In order to speedup the performance of system, we store the whole matrix into redis, so that the front end service is able to quickly fetch the recommended movies for user.

### Evaluation

We use RMSE (Root Mean Square Error) and Accuracy to evaluate our system. RMSE rmse means the average difference to the right rating, so it displays how we are close the right rating. `Accuracy = 1 - RMSE/5`.

If RMSE is 0, that means there is no loss/error, so accurracy is 1 (or 100%); If RMSE is 5 which is highest rating user can give to a movie, there will be high loss/error, so accuracy is 0 (or 0%); RMSE could be negative, that means we are so far away from the right rating, which is not what want, but it can still display we are far away from the right answer.

In the end, we are able to reach more than 80% accuracy.

### Cold Start problem

When new users comes in our system, there is little rating from them. We can not directly find what they like. But we can use statistic knowledge to solve this problem. We recommend top N movies from all history ratings to them, and recommend top N movies from each genre to them. The benefit of this feature is dealing with users frequent taste change and giving user more interest to explore new movies of new genre.

###  Virtualization

 <p align="left">
 <img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/rmse.png" width=80%>
 </p>
   
 <p align="left">
 <img src="https://github.com/XuejiaoDong/Dynamic_Movie_Recommendation/blob/main/images/accuracy.png" width=80%>
 </p>

>>>>>>> master

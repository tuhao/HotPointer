HotPointer
==========

find weibo Hot Point for food

---

###Run

run Main.java at every day 22:00. and it will collect crawler info & rank than push ranked result to public service

###Test

git clone https://github.com/ApesRise/WeiboCrawler.git  
go build thriftServer.go  
./thriftServer  
  
run TestRankByThriftConn.java  

###config
modify conf/config file

###How To Build
mvn package  
than you can find HotPointer-0.0.1.jar in target  
copy jar,conf/ file to same directory and run this program by   
java -jar HotPointer-0.0.1.jar >> send.log

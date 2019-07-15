FROM base-jdk8
   MAINTAINER linzf <282245889@qq.com>
   RUN mkdir -p /app/www/logs/bg-admin-web-core/log/
   RUN mkdir -p /home/app/
   ADD bg-admin-web-core.jar /home/app/app.jar
   CMD java -jar -Xms128m -Xmx1536m /home/app/app.jar
FROM amazoncorretto:21

WORKDIR /app

ADD target/coupon-0.0.1-SNAPSHOT.jar coupon.jar

ENTRYPOINT ["java", "-jar", "coupon.jar"]

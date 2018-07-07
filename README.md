# ObjectRecognizer

Prototype app developed for university course *Development of Applications With Multilayer Architecture*.

### Idea

User uploads an image from their smartphone and the application suggests the most probable name (word) for the object in the picture.

### Systems & technologies

- Thin client: [Android](https://www.android.com/), [ORMLite](http://ormlite.com/)
 ([see dependencies setup](https://github.com/ludgo/ObjectRecognizer/blob/master/vava-client/app/build.gradle#L26))
- Application server: [Spring Boot](https://spring.io/projects/spring-boot), Java Persistence API
 ([see dependencies setup](https://github.com/ludgo/ObjectRecognizer/blob/master/vava-backend/build.gradle#L28); [Java SDK dependencies](https://github.com/ludgo/ObjectRecognizer/blob/master/vava-client/vavalibrary/build.gradle#L7))
- Database server: [PostgreSQL](https://www.postgresql.org/)
- Authentication provider: [Firebase Auth](https://firebase.google.com/docs/auth/admin/)
- Image storage cloud service: [Amazon S3](https://aws.amazon.com/s3/)
- Image recognition provider: [Clarifai API](https://www.clarifai.com/)

#### Development

Java, XML, JSON, Android Studio, IntelliJ IDEA, Gradle

#### Additional libraries

[Lombok](https://projectlombok.org/), JavaX Annotation, [MultiImagePicker](https://github.com/yazeed44/MultiImagePicker), [Picasso](http://square.github.io/picasso/), [Retrofit](http://square.github.io/retrofit/), [GSON](https://github.com/google/gson)

### Architecture

client/server, client SDK, REST JSON, object-relational mapping, cache database

![architecture](https://github.com/ludgo/ObjectRecognizer/blob/master/systems-interaction.png)

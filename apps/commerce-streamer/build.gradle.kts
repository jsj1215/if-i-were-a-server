dependencies {
    implementation(project(":modules:jpa"))
    implementation(project(":modules:redis"))
    implementation(project(":modules:kafka"))
    implementation(project(":modules:monitoring"))

    // 컨슈머 앱이지만 Prometheus 가 지표를 가져갈 수 있도록 웹 서버를 띄운다.
    implementation("org.springframework.boot:spring-boot-starter-web")
}

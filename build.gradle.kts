plugins {
	id("dev.frozenmilk.teamcode") version "10.1.1-0.1.3"
}
repositories {
	maven {
		url = uri("https://jitpack.io")
	}
	maven {
		url = uri("https://maven.brott.dev/")
	}
}
dependencies {
	implementation("dev.frozenmilk.mercurial:Mercurial:1.0.2")
	implementation("dev.frozenmilk.dairy:Core:1.0.1")
	implementation("dev.frozenmilk.dairy:Util:1.1.0")
	implementation("com.acmerobotics.dashboard:dashboard:0.4.16")
	implementation("org.ftclib.ftclib:core:2.1.1")
}
ftc {
	// use this to easily add more FTC libraries
	// adds support for kotlin
	kotlin
}

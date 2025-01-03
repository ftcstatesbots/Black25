plugins {
	id("dev.frozenmilk.teamcode") version "10.1.1-0.1.3"
}
dependencies {
	implementation("dev.frozenmilk.mercurial:Mercurial:1.0.2")
	implementation("dev.frozenmilk.dairy:Core:1.0.1")
	implementation("dev.frozenmilk.dairy:Util:1.1.0")
}
ftc {
	// use this to easily add more FTC libraries
	// adds support for kotlin
	kotlin
}

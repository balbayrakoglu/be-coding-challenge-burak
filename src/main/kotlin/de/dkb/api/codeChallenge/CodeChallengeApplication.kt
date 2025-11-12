package de.dkb.api.codeChallenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class CodeChallengeApplication

fun main(args: Array<String>) {
	runApplication<CodeChallengeApplication>(*args)
}

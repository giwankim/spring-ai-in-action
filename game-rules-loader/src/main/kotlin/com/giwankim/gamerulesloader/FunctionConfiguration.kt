package com.giwankim.gamerulesloader

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.document.Document
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

private val logger = KotlinLogging.logger {}

@Configuration
class FunctionConfiguration {
    @Bean
    fun documentReader(): (Flux<ByteArray>) -> Flux<Document> {
        return { resourceFlux ->
            resourceFlux
                .map { filesByte ->
                    TikaDocumentReader(ByteArrayResource(filesByte))
                        .get()
                        .first()
                }
                .subscribeOn(Schedulers.boundedElastic())
        }
    }

    @Bean
    fun splitter(): (Flux<Document>) -> Flux<List<Document>> {
        val splitter = TokenTextSplitter()
        return { documentFlux ->
            documentFlux
                .map { incoming ->
                    splitter.apply(listOf(incoming))
                }
                .subscribeOn(Schedulers.boundedElastic())
        }
    }
}

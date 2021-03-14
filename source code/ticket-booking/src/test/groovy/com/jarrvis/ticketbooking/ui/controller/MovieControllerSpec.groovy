//package com.jarrvis.ticketbooking.ui.controller
//
//import com.jarrvis.ticketbooking.application.service.MovieService
//import com.jarrvis.ticketbooking.ui.configuration.ApplicationConfig
//import com.jarrvis.ticketbooking.ui.configuration.WebSecurityConfig
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.FilterType
//import org.springframework.test.web.servlet.MockMvc
//import reactor.core.publisher.Flux
//import spock.lang.Specification
//import spock.mock.DetachedMockFactory
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
//
//@WebMvcTest(controllers = [MovieController],
//        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [ApplicationConfig.class, WebSecurityConfig.class, GlobalExceptionHandlingControllerAdvice.class]))
//class MovieControllerSpec extends Specification {
//
//    @Autowired
//    protected MockMvc mvc
//
//    @Autowired
//    MovieService movieService
//
//    def "should call movies api without auth and return 401"() {
//        given:
//            movieService.listAllMovies() >> Flux.empty()
//        when:
//            def response = mvc.perform(post("/movies")).andExpect(request().asyncStarted())andReturn()
//            //response.getAsyncResult(5000L)
//            def result = mvc.perform(asyncDispatch(response))
//
//        then:
//            result.andExpect(status().isUnauthorized())
//    }
//
//
//    @TestConfiguration
//    static class StubConfig {
//        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()
//
//        @Bean
//        MovieService movieService() {
//            return detachedMockFactory.Stub(MovieService)
//        }
//    }
//}
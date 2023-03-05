package com.github.ryebook.common.config

//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.statemachine.config.EnableStateMachineFactory
//import org.springframework.statemachine.config.StateMachineConfigurerAdapter
//import org.springframework.statemachine.config.builders.StateMachineModelConfigurer
//import org.springframework.statemachine.config.model.StateMachineModelFactory
//import org.springframework.statemachine.data.RepositoryState
//import org.springframework.statemachine.data.RepositoryStateMachineModelFactory
//import org.springframework.statemachine.data.RepositoryTransition
//import org.springframework.statemachine.data.StateRepository
//import org.springframework.statemachine.data.TransitionRepository

//@Configuration
//@EnableStateMachineFactory
//class CustomStateMachineConfiguration(
//    private val stateRepository: StateRepository<RepositoryState>,
//    private val transitionRepository: TransitionRepository<RepositoryTransition>
//) : StateMachineConfigurerAdapter<String, String>() {
//
//    override fun configure(model: StateMachineModelConfigurer<String, String>) {
//        model.withModel().factory(productModelFactory())
//    }
//
//    @Bean
//    fun productModelFactory(): StateMachineModelFactory<String, String> {
//        return RepositoryStateMachineModelFactory(stateRepository, transitionRepository)
//    }
//}

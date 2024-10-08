package com.appswave.newsmanagement.config;

import com.appswave.newsmanagement.util.NewsEvents;
import com.appswave.newsmanagement.util.NewsState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<NewsState, NewsEvents> {
    @Override
    public void configure(StateMachineStateConfigurer<NewsState, NewsEvents> states) throws Exception {
        states
                .withStates()
                .initial(NewsState.PENDING)
                .states(EnumSet.allOf(NewsState.class))
                .end(NewsState.APPROVED)
                .end(NewsState.REJECTED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<NewsState, NewsEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(NewsState.PENDING).target(NewsState.APPROVED).event(NewsEvents.APPROVE)
                .and()
                .withExternal()
                .source(NewsState.PENDING).target(NewsState.REJECTED).event(NewsEvents.REJECT);
    }
}
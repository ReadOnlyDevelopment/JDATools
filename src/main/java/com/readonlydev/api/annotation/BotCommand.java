package com.readonlydev.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.readonlydev.command.CommandType;

@Retention(RUNTIME)
@Target(TYPE)
public @interface BotCommand
{
	CommandType value() default CommandType.CONVENTIONAL;
}

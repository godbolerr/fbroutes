package com.work.fb;

public class Constants {
	
	public static final String CURRENT_VERSION = "/v1";
	public static final String HEALTH_CHECK_PATH =  CURRENT_VERSION + "/health";
	public static final String OK = "OK";
	
	public static final int NOT_STARTED = 0 ;
	public static final int INPROGRESS = 1;
	public static final int COMPLETED = 2;
	
	public static final String SCHEMA_TYPE_SOURCE = "S";
	public static final String SCHEMA_TYPE_TARGET = "T";
	
	public static final String SOURCE = "src";
	public static final String TARGET = "trg";	
	
	
	   //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    // Spring profiles for development, test and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_SWAGGER = "swagger";
    // Spring profile used to disable running liquibase
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    public static final String SYSTEM_ACCOUNT = "system";	
	

}

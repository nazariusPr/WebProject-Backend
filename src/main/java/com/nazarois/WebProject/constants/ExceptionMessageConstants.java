package com.nazarois.WebProject.constants;

public class ExceptionMessageConstants {
  private ExceptionMessageConstants() {}

  public static String WRONG_EMAIL_MESSAGE = "User with such email was not found!";
  public static String USER_NOT_FOUND = "User was not found!";
  public static String PASSWORD_NOT_FOUND = "Password was not found!";
  public static String BAD_CREDENTIALS_MESSAGE = "Bad credentials!";
  public static String INVALID_TOKEN_MESSAGE = "Invalid Token";
  public static String ENTITY_NOT_FOUND_MESSAGE = "Entity with such value = %s was not found!";
  public static final String USER_NOT_VERIFIED_MESSAGE = "User is not verified!";
  public static final String FILE_UPLOADING_ERROR_MESSAGE = "Error while uploading file to S3";
  public static final String FILE_DELETING_ERROR_MESSAGE = "Error deleting the file";
  public static final String ACTION_CANCELLATION_MESSAGE = "Action was cancelled";
  public static final String ACTION_CANCELLATION_BAD_REQUEST_MESSAGE =
      "Action is already finished or cancelled";
  public static final String ACTION_IS_NOT_FINISHED = "Action is not finished";
}

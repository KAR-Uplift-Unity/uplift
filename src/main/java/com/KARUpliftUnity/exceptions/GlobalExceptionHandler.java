package com.KARUpliftUnity.exceptions;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", "An unexpected error occurred. Please try again later.");
        System.out.println("error = " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(MailException.class)
    public ModelAndView handleMailException(MailException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", "There was an issue sending the email. Please try again later.");
        System.out.println("error = " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", "The specified username was not found. Please check your credentials and try again.");
        System.out.println("error = " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ModelAndView handleJDBCConnectionException(JDBCConnectionException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", "We're experiencing technical difficulties. Please try again later.");
        System.out.println("error = " + ex.getMessage());
        return modelAndView;
    }
}
